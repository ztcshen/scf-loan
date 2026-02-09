#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Database Field Manager
Manage fields in generated Java code without overwriting files.
"""

import os
import re
import click
from datetime import datetime

class FieldManager:
    def __init__(self, base_package="com.scf.loan", project_root="."):
        self.base_package = base_package
        self.project_root = project_root
        
    def camel_case(self, s):
        """下划线转驼峰"""
        parts = s.split("_")
        return parts[0] + "".join(x.title() for x in parts[1:])
    
    def pascal_case(self, s):
        """下划线转大驼峰"""
        parts = s.split("_")
        if parts and (parts[0] == 't' or parts[0] == 'tbl'):
            parts = parts[1:]
        if parts and parts[0] == 'scf':
            parts = parts[1:]
        return ''.join(x.title() for x in parts)

    def get_java_type(self, mysql_type):
        """MySQL类型转Java类型"""
        type_mapping = {
            "int": "Integer",
            "bigint": "Long",
            "varchar": "String",
            "text": "String",
            "char": "String",
            "datetime": "LocalDateTime",
            "timestamp": "LocalDateTime",
            "date": "LocalDate",
            "decimal": "BigDecimal",
            "double": "Double",
            "float": "Float",
            "tinyint": "Integer",
            "boolean": "Boolean",
            "bit": "Boolean"
        }
        
        # 提取基本类型
        try:
            base_type = re.match(r"([a-z]+)", mysql_type.lower()).group(1)
            return type_mapping.get(base_type, "String")
        except (AttributeError, TypeError):
            return "String"

    def get_import_for_type(self, java_type):
        """获取Java类型的Import语句"""
        imports = {
            "BigDecimal": "java.math.BigDecimal",
            "LocalDateTime": "java.time.LocalDateTime",
            "LocalDate": "java.time.LocalDate",
            "Date": "java.util.Date",
            "List": "java.util.List"
        }
        return imports.get(java_type)

    def find_file(self, module, sub_package, class_name):
        """查找Java文件路径"""
        path_parts = self.base_package.split(".")
        path = os.path.join(
            self.project_root,
            module,
            "src",
            "main",
            "java",
            *path_parts,
            *sub_package.split("."),
            f"{class_name}.java"
        )
        return path if os.path.exists(path) else None

    def add_import_if_needed(self, content, java_type):
        """添加Import语句"""
        import_stmt = self.get_import_for_type(java_type)
        if not import_stmt:
            return content
            
        if f"import {import_stmt};" in content:
            return content
            
        # 找到最后一个import语句，插在后面
        lines = content.split('\n')
        last_import_idx = -1
        package_idx = -1
        
        for i, line in enumerate(lines):
            if line.strip().startswith('package '):
                package_idx = i
            if line.strip().startswith('import '):
                last_import_idx = i
        
        insert_pos = last_import_idx if last_import_idx != -1 else package_idx
        
        if insert_pos != -1:
            lines.insert(insert_pos + 1, f"import {import_stmt};")
            # 如果是package后第一次插入import，加个空行
            if last_import_idx == -1:
                lines.insert(insert_pos + 1, "")
                
        return '\n'.join(lines)

    def add_field_to_class(self, file_path, field_name, java_type, comment):
        """向类中添加字段"""
        if not file_path:
            print(f"Warning: File not found for field addition.")
            return

        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # 检查字段是否已存在
        if f"private {java_type} {field_name};" in content:
            print(f"Field {field_name} already exists in {os.path.basename(file_path)}")
            return

        # 添加Import
        content = self.add_import_if_needed(content, java_type)
        
        # 构建字段定义
        field_code = ""
        if comment:
            field_code += f"    /**\n     * {comment}\n     */\n"
        field_code += f"    private {java_type} {field_name};\n"

        # 寻找插入点：最后一个字段之后
        # 匹配 private Type name;
        field_pattern = re.compile(r"^\s*private\s+[\w<>, ]+\s+\w+;", re.MULTILINE)
        matches = list(field_pattern.finditer(content))
        
        if matches:
            last_match = matches[-1]
            insert_pos = last_match.end()
            content = content[:insert_pos] + "\n\n" + field_code + content[insert_pos:]
        else:
            # 如果没有字段，找类定义的开始
            class_pattern = re.compile(r"public\s+class\s+\w+\s*(?:extends\s+\w+)?\s*(?:implements\s+[\w, ]+)?\s*\{")
            match = class_pattern.search(content)
            if match:
                insert_pos = match.end()
                content = content[:insert_pos] + "\n\n" + field_code + content[insert_pos:]
            else:
                print(f"Error: Could not find insertion point in {file_path}")
                return

        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Added field {field_name} to {os.path.basename(file_path)}")

    def remove_field_from_class(self, file_path, field_name):
        """从类中移除字段"""
        if not file_path:
            return

        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()

        new_lines = []
        skip_comment = False
        field_removed = False
        
        i = 0
        while i < len(lines):
            line = lines[i]
            stripped = line.strip()
            
            # 检测字段定义
            # private Type fieldName;
            if re.match(r"private\s+[\w<>, ]+\s+" + field_name + r";", stripped):
                # 如果前几行是注释，移除注释
                # 回溯移除 new_lines 中的注释
                # 简单的Javadoc注释通常是 /** ... */
                # 这里简单处理：如果上一行是 */，则向上移除直到 /**
                if new_lines and new_lines[-1].strip() == "*/":
                    while new_lines and "/**" not in new_lines[-1]:
                        new_lines.pop()
                    if new_lines: new_lines.pop() # remove /**
                
                # 移除单行注释
                elif new_lines and (new_lines[-1].strip().startswith("//") or new_lines[-1].strip().startswith("*")):
                     # 这是一个简化的假设，可能误删，但通常生成的代码结构规范
                     pass
                
                field_removed = True
                i += 1
                continue
                
            new_lines.append(line)
            i += 1

        if field_removed:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.writelines(new_lines)
            print(f"Removed field {field_name} from {os.path.basename(file_path)}")
        else:
            print(f"Field {field_name} not found in {os.path.basename(file_path)}")

    def update_converter_add(self, file_path, field_name, method_suffix):
        """更新Converter：添加映射"""
        if not file_path: return
        
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        # 1. Update toDTO
        # d.setFieldName(e.getFieldName());
        # Find insertion point in toDTO method
        # Look for "return d;" inside toDTO
        match = re.search(r"(public\s+static\s+\w+DTO\s+toDTO\(.*?\)\s*\{.*?)(\s*)return\s+d;", content, re.DOTALL)
        if match:
            # group(1) is everything up to indentation
            # group(2) is the indentation before "return d;"
            indent = match.group(2)
            # Check if mapping already exists to avoid duplicates
            setter_stmt = f"d.set{method_suffix}(e.get{method_suffix}());"
            if setter_stmt not in match.group(1):
                # Insert before return d;
                # We need to construct the insertion string
                # We want: indent + setter_stmt + \n + indent + return d;
                # But we are replacing the whole match? No, regex replace is easier.
                
                insertion = f"{indent}{setter_stmt}\n{indent}return d;"
                # Replace the end of the match (indent + return d;) with insertion
                # Since we captured indent and "return d;" is literal in regex (mostly)
                # Let's use string replacement on the exact found string if unique enough, 
                # or use span from match.
                
                # Better: use the match end to locate where "return d;" is.
                # match.end() is after "return d;". 
                # match.group(2) start is where indent starts.
                indent_start = match.start(2)
                
                # Insert at indent_start
                content = content[:indent_start] + f"{indent}{setter_stmt}\n" + content[indent_start:]

        # 2. Update toEntity
        # e.setFieldName(d.getFieldName());
        # Re-read content or continue? Continue with modified content.
        # Note: offsets might have changed if we modified content.
        # So we should search again or handle offset shift. 
        # Since toEntity is after toDTO usually, searching from beginning is fine, 
        # but to be safe, let's search specifically for toEntity.
        
        match = re.search(r"(public\s+static\s+\w+Entity\s+toEntity\(.*?\)\s*\{.*?)(\s*)return\s+e;", content, re.DOTALL)
        if match:
            indent = match.group(2)
            setter_stmt = f"e.set{method_suffix}(d.get{method_suffix}());"
            if setter_stmt not in match.group(1):
                indent_start = match.start(2)
                content = content[:indent_start] + f"{indent}{setter_stmt}\n" + content[indent_start:]

        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated Converter {os.path.basename(file_path)}")

    def update_converter_remove(self, file_path, method_suffix):
        """更新Converter：移除映射"""
        if not file_path: return
        
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
            
        new_lines = []
        for line in lines:
            if f".set{method_suffix}(" in line and f".get{method_suffix}()" in line:
                continue
            new_lines.append(line)
            
        with open(file_path, 'w', encoding='utf-8') as f:
            f.writelines(new_lines)
        print(f"Updated Converter {os.path.basename(file_path)} (removed mapping)")

    def generate_alter_sql(self, table, field, mysql_type, comment):
        """生成ALTER TABLE语句并保存"""
        comment_sql = f" COMMENT '{comment}'" if comment else ""
        sql = f"ALTER TABLE `{table}` ADD COLUMN `{field}` {mysql_type}{comment_sql};\n"
        
        # Ensure sql directory exists
        sql_dir = os.path.join(os.path.dirname(__file__), "sql")
        os.makedirs(sql_dir, exist_ok=True)
        
        # Save to file
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
        filename = f"{timestamp}_alter_{table}_add_{field}.sql"
        file_path = os.path.join(sql_dir, filename)
        
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(sql)
            
        print(f"Generated SQL: {file_path}")
        return file_path

    def process_add(self, table, field, mysql_type, comment):
        # 0. Generate SQL
        self.generate_alter_sql(table, field, mysql_type, comment)

        entity_name = self.pascal_case(table)
        if entity_name.endswith("Entity"): entity_name = entity_name[:-6]
        entity_name += "Entity"
        
        field_camel = self.camel_case(field)
        java_type = self.get_java_type(mysql_type)
        method_suffix = field_camel[0].upper() + field_camel[1:] if field_camel else ""
        
        # 1. Entity
        entity_path = self.find_file("scf-loan-dal", "dal.entity", entity_name)
        self.add_field_to_class(entity_path, field_camel, java_type, comment)
        
        # 2. DTO
        dto_name = entity_name.replace("Entity", "DTO")
        dto_path = self.find_file("scf-loan-common", "common.dto", dto_name)
        self.add_field_to_class(dto_path, field_camel, java_type, comment)
        
        # 3. Converter
        converter_name = entity_name.replace("Entity", "Converter")
        converter_path = self.find_file("scf-loan-biz", "biz.convert", converter_name)
        self.update_converter_add(converter_path, field_camel, method_suffix)

    def process_remove(self, table, field):
        entity_name = self.pascal_case(table)
        if entity_name.endswith("Entity"): entity_name = entity_name[:-6]
        entity_name += "Entity"
        
        field_camel = self.camel_case(field)
        method_suffix = field_camel[0].upper() + field_camel[1:] if field_camel else ""
        
        # 1. Entity
        entity_path = self.find_file("scf-loan-dal", "dal.entity", entity_name)
        self.remove_field_from_class(entity_path, field_camel)
        
        # 2. DTO
        dto_name = entity_name.replace("Entity", "DTO")
        dto_path = self.find_file("scf-loan-common", "common.dto", dto_name)
        self.remove_field_from_class(dto_path, field_camel)
        
        # 3. Converter
        converter_name = entity_name.replace("Entity", "Converter")
        converter_path = self.find_file("scf-loan-biz", "biz.convert", converter_name)
        self.update_converter_remove(converter_path, method_suffix)

@click.group()
def cli():
    pass

@cli.command("add")
@click.option("--table", required=True, help="Table name")
@click.option("--field", required=True, help="Column name")
@click.option("--type", required=True, help="MySQL column type")
@click.option("--comment", default="", help="Column comment")
@click.option("--base-package", default="com.scf.loan", help="Base package")
def add_field(table, field, type, comment, base_package):
    """Add a field to Entity, DTO and Converter"""
    manager = FieldManager(base_package, ".")
    manager.process_add(table, field, type, comment)

@cli.command("remove")
@click.option("--table", required=True, help="Table name")
@click.option("--field", required=True, help="Column name")
@click.option("--base-package", default="com.scf.loan", help="Base package")
def remove_field(table, field, base_package):
    """Remove a field from Entity, DTO and Converter"""
    manager = FieldManager(base_package, ".")
    manager.process_remove(table, field)

if __name__ == "__main__":
    cli()
