#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Enum Generator - 数据库枚举生成器
"""

import os
import sys
import re
import yaml
import click
import pymysql
from jinja2 import Template, FileSystemLoader, Environment

class EnumGenerator:
    def __init__(self, db_url, username, password, base_package="com.scf.loan", output_dir="."):
        self.db_url = db_url
        self.username = username
        self.password = password
        self.base_package = base_package
        self.output_dir = output_dir
        self.templates_dir = os.path.join(os.path.dirname(__file__), "templates")
        self.env = Environment(loader=FileSystemLoader(self.templates_dir), trim_blocks=True, lstrip_blocks=True)

    def connect(self):
        try:
            match = re.match(r"mysql://([^:]+):(\d+)/([^?]+)", self.db_url)
            if not match: raise ValueError("Invalid DB URL")
            host, port, database = match.groups()
            return pymysql.connect(
                host=host, port=int(port), user=self.username, password=self.password,
                database=database, charset='utf8mb4', cursorclass=pymysql.cursors.DictCursor
            )
        except Exception as e:
            print(f"Connection failed: {e}")
            return None

    def parse_enum_items(self, comment):
        """解析注释中的枚举项"""
        items = []
        # 常见模式:
        # 1. 审批状态 审批中(applying)，审批失败(refused)
        # 2. 删除标志 0:有效 1:无效
        
        # 策略1：匹配 英文/中文名称(code)
        # 排除 "yyyy-MM-dd" 这种日期格式说明
        if "yyyy" in comment: return []

        # 正则1: label(code) 或 label（code）
        matches1 = re.findall(r"([\u4e00-\u9fa5\w]+)[（\(]([a-zA-Z0-9_]+)[）\)]", comment)
        if matches1:
            for label, code in matches1:
                # 过滤掉单纯的英文解释
                if label == code: continue
                items.append({"code": code, "label": label, "type": "String"})
            return self.format_items(items)

        # 正则2: code:label 或 code=label
        matches2 = re.findall(r"(\d+)[\s:=-]+([\u4e00-\u9fa5\w]+)", comment)
        if matches2:
            for code, label in matches2:
                items.append({"code": code, "label": label, "type": "Integer"})
            return self.format_items(items)
            
        return []

    def format_items(self, items):
        """格式化枚举项名称"""
        formatted = []
        for item in items:
            code = item["code"]
            # 如果code是数字，尝试转成英文描述（这里简单处理，实际可能需要映射表或用户干预）
            # 这里对于数字code，暂时生成 ITEM_0, ITEM_1 这种形式，或者让用户后续修改
            name = code.upper()
            if re.match(r"^\d+$", name):
                name = f"STATUS_{name}"
            
            # 如果code是字符串，转大写下划线
            if not re.match(r"^\d+$", code):
                name = re.sub(r"([a-z0-9])([A-Z])", r"\1_\2", code).upper()
            
            # 确定代码类型
            code_val = f'"{code}"' if item["type"] == "String" else code
            
            formatted.append({
                "name": name,
                "code": code_val,
                "label": item["label"],
                "raw_code": code
            })
        return formatted

    def generate(self, table_name, column_name=None, ddl_content=None):
        """生成枚举类，支持从数据库或DDL内容生成"""
        print(f"DEBUG: Starting generation for table {table_name}")
        if ddl_content:
            print("DEBUG: Using DDL content")
            self._generate_from_ddl(ddl_content, table_name, column_name)
        else:
            print("DEBUG: Using DB connection")
            self._generate_from_db(table_name, column_name)

    def _generate_from_db(self, table_name, column_name):
        conn = self.connect()
        if not conn: return
        try:
            with conn.cursor() as cursor:
                sql = f"SHOW FULL COLUMNS FROM `{table_name}`"
                cursor.execute(sql)
                columns = cursor.fetchall()
                self._process_columns(columns, column_name)
        finally:
            conn.close()

    def _generate_from_ddl(self, ddl_content, table_name, column_name):
        """从DDL内容解析列信息并生成枚举"""
        columns = []
        # 简单解析DDL中的列定义和注释
        # 匹配模式: `column_name` type ... COMMENT 'comment'
        pattern = re.compile(r"`([^`]+)`\s+([^\s,]+).*?COMMENT\s+'([^']+)'", re.IGNORECASE)
        for match in pattern.finditer(ddl_content):
            columns.append({
                "Field": match.group(1),
                "Type": match.group(2),
                "Comment": match.group(3)
            })
        self._process_columns(columns, column_name)

    def _process_columns(self, columns, column_name):
        """处理列信息并生成枚举"""
        print(f"DEBUG: Processing {len(columns)} columns...")
        for col in columns:
            field = col["Field"]
            if column_name and field != column_name: continue
            
            comment = col["Comment"]
            if not comment: continue
            
            items = self.parse_enum_items(comment)
            if not items: 
                print(f"DEBUG: No enum items found for field {field} with comment: {comment}")
                continue
            
            print(f"Found enum candidate: {field} -> {items}")
            
            # 生成枚举名：Status -> ApplyStatusEnum
            # 去掉 _id, _status 后缀
            base_name = field
            if base_name.endswith("_status"): base_name = base_name[:-7]
            if base_name.endswith("_type"): base_name = base_name[:-5]
            
            # 转帕斯卡命名
            parts = base_name.split("_")
            pascal_name = "".join(x.title() for x in parts)
            enum_name = f"{pascal_name}Enum"
            
            # 确定 code 类型
            code_type = "String"
            if items and not items[0]["code"].startswith('"'):
                code_type = "Integer"
            
            # 渲染
            template = self.env.get_template("enum_template.java.j2")
            content = template.render(
                package=f"{self.base_package}.common.enums",
                enum_name=enum_name,
                enum_comment=comment.split(" ")[0], # 取注释的第一部分作为类说明
                items=items,
                code_type=code_type
            )
            
            # 输出文件
            output_path = os.path.join(
                self.output_dir,
                "scf-loan-common", "src", "main", "java",
                *self.base_package.split("."), "common", "enums"
            )
            os.makedirs(output_path, exist_ok=True)
            file_path = os.path.join(output_path, f"{enum_name}.java")
            
            with open(file_path, "w", encoding="utf-8") as f:
                f.write(content)
            print(f"Generated Enum: {file_path}")

@click.command()
@click.option('--table', required=True, help='Table Name')
@click.option('--column', default=None, help='Column Name')
@click.option('--db-url', default='mysql://localhost:3306/scf_loan', help='Database URL')
@click.option('--db-username', default='root', help='Database Username')
@click.option('--db-password', default='123456', help='Database Password')
@click.option('--output-dir', default='.', help='Output Directory')
def main(table, column, db_url, db_username, db_password, output_dir):
    # 尝试加载配置文件
    config_path = os.path.join(os.path.dirname(__file__), "..", "db-generate", "config.yml")
    if os.path.exists(config_path):
        with open(config_path, 'r') as f:
            config = yaml.safe_load(f)
            if db_url == 'mysql://localhost:3306/scf_loan': db_url = config.get('db_url', db_url)
            if db_username == 'root': db_username = config.get('db_username', db_username)
            if db_password == '123456': db_password = config.get('db_password', db_password)

    generator = EnumGenerator(db_url, db_username, db_password, output_dir=output_dir)
    generator.generate(table, column)

if __name__ == '__main__':
    main()
