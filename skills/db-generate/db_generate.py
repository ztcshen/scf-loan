#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
数据库表代码生成工具
根据数据库表结构自动生成实体类、Mapper、Service、Controller 等基础代码
并提供代码验证和单元测试生成功能
"""

import os
import re
import json
import yaml
import click
import pymysql
from jinja2 import Template, FileSystemLoader, Environment


class DatabaseGenerator:
    def __init__(self, db_url, username, password, base_package="com.scf.loan", output_dir="."):
        """初始化数据库连接"""
        self.db_url = db_url
        self.username = username
        self.password = password
        self.base_package = base_package
        self.output_dir = output_dir
        self.db = None
        self.cursor = None
        self.templates_dir = os.path.join(os.path.dirname(__file__), "templates")
        
        # 创建模板环境
        self.env = Environment(
            loader=FileSystemLoader(self.templates_dir),
            trim_blocks=True,
            lstrip_blocks=True
        )
    
    def connect(self):
        """连接数据库"""
        try:
            # 解析数据库连接URL
            # 格式: mysql://host:port/database
            match = re.match(r"mysql://([^:]+):(\d+)/([^?]+)", self.db_url)
            if not match:
                raise ValueError("Invalid database URL format. Expected: mysql://host:port/database")
            
            host, port, database = match.groups()
            port = int(port)
            
            self.db = pymysql.connect(
                host=host,
                port=port,
                user=self.username,
                password=self.password,
                database=database,
                charset="utf8mb4",
                cursorclass=pymysql.cursors.DictCursor
            )
            self.cursor = self.db.cursor()
            print(f"Connected to database: {database}")
        except Exception as e:
            print(f"Failed to connect to database: {e}")
            raise
    
    def disconnect(self):
        """断开数据库连接"""
        if self.cursor:
            self.cursor.close()
        if self.db:
            self.db.close()
    
    def get_table_schema(self, table_name):
        """获取表结构"""
        try:
            # 获取表注释
            self.cursor.execute(f"SHOW TABLE STATUS LIKE '{table_name}'")
            table_info = self.cursor.fetchone()
            table_comment = table_info.get("Comment", "") if table_info else ""
            
            # 获取列信息
            self.cursor.execute(f"SHOW FULL COLUMNS FROM `{table_name}`")
            columns = self.cursor.fetchall()
            
            return {
                "table_name": table_name,
                "table_comment": table_comment,
                "columns": columns
            }
        except Exception as e:
            print(f"Failed to get table schema: {e}")
            raise
    
    def camel_case(self, s):
        """下划线转驼峰"""
        parts = s.split("_")
        return parts[0] + "".join(x.title() for x in parts[1:])
    
    def pascal_case(self, s):
        """下划线转大驼峰（帕斯卡命名法）"""
        parts = s.split("_")
        # 去除常见的表名前缀，如t_、tbl_等
        if parts and (parts[0] == 't' or parts[0] == 'tbl'):
            parts = parts[1:]
        # 去除模块前缀，如scf_
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
            "datetime": "LocalDateTime",
            "date": "LocalDate",
            "decimal": "BigDecimal",
            "tinyint": "Integer",
            "boolean": "Boolean"
        }
        
        # 提取基本类型
        try:
            base_type = re.match(r"([a-z]+)", mysql_type).group(1)
            return type_mapping.get(base_type, "String")
        except (AttributeError, TypeError):
            # 如果无法提取基本类型，默认返回String
            return "String"
    
    def generate_entity(self, table_schema):
        """生成实体类"""
        table_name = table_schema["table_name"]
        table_comment = table_schema["table_comment"]
        columns = table_schema["columns"]
        
        # 生成实体类名
        entity_name = self.pascal_case(table_name)
        if entity_name.endswith("Entity"):
            entity_name = entity_name[:-6]
        entity_name += "Entity"
        
        # 生成字段信息
        fields = []
        for column in columns:
            column_name = column["Field"]
            column_type = column["Type"]
            column_comment = column["Comment"]
            java_type = self.get_java_type(column_type)
            
            fields.append({
                "column_name": column_name,
                "field_name": self.camel_case(column_name),
                "java_type": java_type,
                "comment": column_comment,
                "is_id": column_name == "id"
            })
        
        # 渲染模板
        template = self.env.get_template("entity_template.java.j2")
        content = template.render(
            package=f"{self.base_package}.dal.entity",
            entity_name=entity_name,
            table_name=table_name,
            table_comment=table_comment,
            fields=fields
        )
        
        # 生成文件路径
        output_path = os.path.join(
            self.output_dir,
            "scf-loan-dal",
            "src",
            "main",
            "java",
            *self.base_package.split("."),
            "dal",
            "entity"
        )
        os.makedirs(output_path, exist_ok=True)
        
        file_path = os.path.join(output_path, f"{entity_name}.java")
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)
        
        print(f"Generated entity: {file_path}")
        return entity_name
    
    def generate_mapper(self, entity_name, table_name):
        """生成Mapper接口"""
        mapper_name = entity_name.replace("Entity", "Mapper")
        
        # 渲染模板
        template = self.env.get_template("mapper_template.java.j2")
        content = template.render(
            package=f"{self.base_package}.dal.mapper",
            mapper_name=mapper_name,
            entity_name=entity_name,
            entity_package=f"{self.base_package}.dal.entity"
        )
        
        # 生成文件路径
        output_path = os.path.join(
            self.output_dir,
            "scf-loan-dal",
            "src",
            "main",
            "java",
            *self.base_package.split("."),
            "dal",
            "mapper"
        )
        os.makedirs(output_path, exist_ok=True)
        
        file_path = os.path.join(output_path, f"{mapper_name}.java")
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)
        
        print(f"Generated mapper: {file_path}")
        return mapper_name
    
    def generate_service(self, entity_name, table_name):
        """生成Service接口"""
        service_name = entity_name.replace("Entity", "Service")
        
        # 渲染模板
        template = self.env.get_template("service_template.java.j2")
        content = template.render(
            package=f"{self.base_package}.biz.service",
            service_name=service_name,
            entity_name=entity_name,
            entity_package=f"{self.base_package}.dal.entity"
        )
        
        # 生成文件路径
        output_path = os.path.join(
            self.output_dir,
            "scf-loan-biz",
            "src",
            "main",
            "java",
            *self.base_package.split("."),
            "biz",
            "service"
        )
        os.makedirs(output_path, exist_ok=True)
        
        file_path = os.path.join(output_path, f"{service_name}.java")
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)
        
        print(f"Generated service: {file_path}")
        return service_name
    
    def generate_service_impl(self, service_name, entity_name, mapper_name):
        """生成Service实现类"""
        service_impl_name = service_name + "Impl"
        
        # 渲染模板
        template = self.env.get_template("service_impl_template.java.j2")
        content = template.render(
            package=f"{self.base_package}.biz.service.impl",
            service_impl_name=service_impl_name,
            service_name=service_name,
            entity_name=entity_name,
            mapper_name=mapper_name,
            service_package=f"{self.base_package}.biz.service",
            entity_package=f"{self.base_package}.dal.entity",
            mapper_package=f"{self.base_package}.dal.mapper"
        )
        
        # 生成文件路径
        output_path = os.path.join(
            self.output_dir,
            "scf-loan-biz",
            "src",
            "main",
            "java",
            *self.base_package.split("."),
            "biz",
            "service",
            "impl"
        )
        os.makedirs(output_path, exist_ok=True)
        
        file_path = os.path.join(output_path, f"{service_impl_name}.java")
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)
        
        print(f"Generated service impl: {file_path}")
    
    def generate_dto(self, entity_name, table_schema):
        """生成DTO类"""
        dto_name = entity_name.replace("Entity", "DTO")
        fields = []
        
        for column in table_schema["columns"]:
            column_name = column["Field"]
            column_type = column["Type"]
            column_comment = column["Comment"]
            java_type = self.get_java_type(column_type)
            
            fields.append({
                "column_name": column_name,
                "field_name": self.camel_case(column_name),
                "java_type": java_type,
                "comment": column_comment
            })
        
        # 渲染模板
        template = self.env.get_template("dto_template.java.j2")
        content = template.render(
            package=f"{self.base_package}.common.dto",
            dto_name=dto_name,
            fields=fields
        )
        
        # 生成文件路径
        output_path = os.path.join(
            self.output_dir,
            "scf-loan-common",
            "src",
            "main",
            "java",
            *self.base_package.split("."),
            "common",
            "dto"
        )
        os.makedirs(output_path, exist_ok=True)
        
        file_path = os.path.join(output_path, f"{dto_name}.java")
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)
        
        print(f"Generated DTO: {file_path}")
        return dto_name
    
    def generate_controller(self, entity_name, service_name, dto_name):
        """生成Controller类"""
        controller_name = entity_name.replace("Entity", "Controller")
        api_path = "/api/" + entity_name.replace("Entity", "").lower()
        
        # 渲染模板
        template = self.env.get_template("controller_template.java.j2")
        content = template.render(
            package=f"{self.base_package}.web.controller",
            controller_name=controller_name,
            api_path=api_path,
            service_name=service_name,
            entity_name=entity_name,
            dto_name=dto_name,
            service_package=f"{self.base_package}.biz.service",
            entity_package=f"{self.base_package}.dal.entity",
            dto_package=f"{self.base_package}.common.dto"
        )
        
        # 生成文件路径
        output_path = os.path.join(
            self.output_dir,
            "scf-loan-web",
            "src",
            "main",
            "java",
            *self.base_package.split("."),
            "web",
            "controller"
        )
        os.makedirs(output_path, exist_ok=True)
        
        file_path = os.path.join(output_path, f"{controller_name}.java")
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)
        
        print(f"Generated controller: {file_path}")
    
    def generate_unit_test(self, entity_name, service_name):
        """生成单元测试类"""
        try:
            test_name = service_name + "Test"
            print(f"Generating unit test for service: {service_name}, test name: {test_name}")
            
            # 渲染模板
            print("Loading template...")
            template = self.env.get_template("unit_test_template.java.j2")
            
            print("Rendering template...")
            content = template.render(
                package=f"{self.base_package}.biz.service.impl",
                test_name=test_name,
                service_name=service_name,
                entity_name=entity_name,
                service_package=f"{self.base_package}.biz.service",
                entity_package=f"{self.base_package}.dal.entity"
            )
            print(f"Template rendered successfully, content length: {len(content)}")
            
            # 生成文件路径
            print(f"Generating output path with base_package: {self.base_package}")
            package_parts = self.base_package.split(".")
            print(f"Package parts: {package_parts}")
            
            output_path = os.path.join(
                self.output_dir,
                "scf-loan-biz",
                "src",
                "test",
                "java",
                *package_parts,
                "biz",
                "service",
                "impl"
            )
            print(f"Output path: {output_path}")
            
            print("Creating directory...")
            os.makedirs(output_path, exist_ok=True)
            print(f"Directory created or exists: {os.path.exists(output_path)}")
            
            file_path = os.path.join(output_path, f"{test_name}.java")
            print(f"Writing file to: {file_path}")
            
            with open(file_path, "w", encoding="utf-8") as f:
                f.write(content)
            
            print(f"File written successfully, size: {os.path.getsize(file_path)} bytes")
            print(f"Generated unit test: {file_path}")
        except Exception as e:
            print(f"Error in generate_unit_test: {e}")
            import traceback
            traceback.print_exc()
    
    def validate_code(self, generated_files):
        """验证生成的代码"""
        print("Validating generated code...")
        
        # 检查文件是否存在
        for file_path in generated_files:
            if os.path.exists(file_path):
                print(f"✓ {os.path.basename(file_path)} exists")
            else:
                print(f"✗ {os.path.basename(file_path)} missing")
        
        # 可以添加更多验证逻辑，比如语法检查等
        print("Code validation completed.")
    
    def parse_table_schema_from_sql(self, table_name, sql):
        """从SQL语句解析表结构"""
        print("Parsing table schema from SQL...")
        
        # 提取表注释
        table_comment_match = re.search(r"COMMENT='([^']+)'", sql)
        table_comment = table_comment_match.group(1) if table_comment_match else ""
        
        # 提取列信息
        columns = []
        
        # 按行解析SQL语句
        lines = sql.split('\n')
        
        for line in lines:
            # 跳过空行和注释行
            line = line.strip()
            if not line or line.startswith('--') or line.startswith('#'):
                continue
            
            # 跳过 CREATE TABLE 语句行
            if line.upper().startswith('CREATE TABLE'):
                continue
            
            # 跳过索引定义
            if any(keyword in line.upper() for keyword in ['PRIMARY KEY', 'KEY ', 'UNIQUE KEY']):
                continue
            
            # 跳过 ENGINE 等表级配置
            if any(keyword in line.upper() for keyword in ['ENGINE=', 'DEFAULT CHARSET=', 'AUTO_INCREMENT=']):
                continue
            
            # 匹配列定义
            # 首先提取字段名和类型
            column_name_pattern = re.compile(r"`([^`]+)`\s+([^\s,]+)")
            name_match = column_name_pattern.search(line)
            
            if name_match:
                column_name = name_match.group(1)
                column_type = name_match.group(2)
                
                # 提取注释
                column_comment = ""
                comment_pattern = re.compile(r"COMMENT\s*['\"]([^'\"]+)['\"]")
                comment_match = comment_pattern.search(line)
                if comment_match:
                    column_comment = comment_match.group(1)
                
                # 跳过索引相关的字段名和类型为括号的情况
                if not any(keyword in column_name for keyword in ['idx_', 'PRIMARY', 'UNIQUE', 'KEY']) and not column_type.startswith('('):
                    columns.append({
                        "Field": column_name,
                        "Type": column_type,
                        "Comment": column_comment
                    })
        
        # 确保id字段被正确处理
        has_id = any(col["Field"] == "id" for col in columns)
        if not has_id:
            # 尝试从SQL语句中单独提取id字段
            for line in lines:
                line = line.strip()
                if '`id`' in line:
                    id_pattern = re.compile(r"`id`\s+([^\s,]+)(?:\s+([^,]+))?\s*(?:COMMENT\s*['\"]([^'\"]+)['\"])?")
                    id_match = id_pattern.search(line)
                    if id_match:
                        columns.insert(0, {
                            "Field": "id",
                            "Type": id_match.group(1),
                            "Comment": id_match.group(3) or "主键ID"
                        })
                    break
        
        print(f"Parsed {len(columns)} columns from DDL")
        for col in columns:
            print(f"  Column: {col['Field']}, Type: {col['Type']}, Comment: {col['Comment']}")
        
        return {
            "table_name": table_name,
            "table_comment": table_comment,
            "columns": columns
        }
    
    def save_ddl(self, table_name, sql):
        """保存DDL语句到文件"""
        ddl_dir = os.path.join(os.path.dirname(__file__), "ddl")
        os.makedirs(ddl_dir, exist_ok=True)
        
        ddl_file = os.path.join(ddl_dir, f"{table_name}.sql")
        with open(ddl_file, "w", encoding="utf-8") as f:
            f.write(sql)
        
        print(f"DDL statement saved to: {ddl_file}")
        return ddl_file
    
    def load_ddl(self, ddl_file):
        """从文件加载DDL语句"""
        if not os.path.exists(ddl_file):
            raise FileNotFoundError(f"DDL file not found: {ddl_file}")
        
        with open(ddl_file, "r", encoding="utf-8") as f:
            sql = f.read()
        
        print(f"DDL statement loaded from: {ddl_file}")
        return sql
    
    def generate_code(self, table_name, ddl_file=None, validate=False, generate_tests=False):
        """生成完整的代码"""
        try:
            # 如果提供了DDL文件，优先使用DDL文件生成代码
            if ddl_file:
                print("Using DDL file to generate code...")
                # 加载DDL文件
                sql = self.load_ddl(ddl_file)
                
                # 保存DDL语句到文件
                self.save_ddl(table_name, sql)
                
                # 从SQL语句解析表结构
                table_schema = self.parse_table_schema_from_sql(table_name, sql)
                
                # 生成完整代码
                entity_name = self.generate_entity(table_schema)
                mapper_name = self.generate_mapper(entity_name, table_name)
                service_name = self.generate_service(entity_name, table_name)
                self.generate_service_impl(service_name, entity_name, mapper_name)
                dto_name = self.generate_dto(entity_name, table_schema)
                self.generate_controller(entity_name, service_name, dto_name)
                
                # 生成单元测试
                if generate_tests:
                    self.generate_unit_test(entity_name, service_name)
                
                # 收集生成的文件
                generated_files = [
                    os.path.join(self.output_dir, "scf-loan-dal", "src", "main", "java", *self.base_package.split("."), "dal", "entity", f"{entity_name}.java"),
                    os.path.join(self.output_dir, "scf-loan-dal", "src", "main", "java", *self.base_package.split("."), "dal", "mapper", f"{mapper_name}.java"),
                    os.path.join(self.output_dir, "scf-loan-biz", "src", "main", "java", *self.base_package.split("."), "biz", "service", f"{service_name}.java"),
                    os.path.join(self.output_dir, "scf-loan-biz", "src", "main", "java", *self.base_package.split("."), "biz", "service", "impl", f"{service_name}Impl.java"),
                    os.path.join(self.output_dir, "scf-loan-common", "src", "main", "java", *self.base_package.split("."), "common", "dto", f"{dto_name}.java"),
                    os.path.join(self.output_dir, "scf-loan-web", "src", "main", "java", *self.base_package.split("."), "web", "controller", f"{entity_name.replace('Entity', 'Controller')}.java")
                ]
                
                if generate_tests:
                    generated_files.append(
                        os.path.join(self.output_dir, "scf-loan-biz", "src", "test", "java", *self.base_package.split("."), "biz", "service", "impl", f"{service_name}Test.java")
                    )
                
                # 验证代码
                if validate:
                    self.validate_code(generated_files)
                
                print("\nCode generation completed successfully based on DDL file!")
            else:
                # 尝试连接数据库
                try:
                    self.connect()
                    # 获取表结构
                    table_schema = self.get_table_schema(table_name)
                    
                    # 生成代码
                    entity_name = self.generate_entity(table_schema)
                    mapper_name = self.generate_mapper(entity_name, table_name)
                    service_name = self.generate_service(entity_name, table_name)
                    self.generate_service_impl(service_name, entity_name, mapper_name)
                    dto_name = self.generate_dto(entity_name, table_schema)
                    self.generate_controller(entity_name, service_name, dto_name)
                    
                    # 生成单元测试
                    if generate_tests:
                        self.generate_unit_test(entity_name, service_name)
                    
                    # 收集生成的文件
                    generated_files = [
                        os.path.join(self.output_dir, "scf-loan-dal", "src", "main", "java", *self.base_package.split("."), "dal", "entity", f"{entity_name}.java"),
                        os.path.join(self.output_dir, "scf-loan-dal", "src", "main", "java", *self.base_package.split("."), "dal", "mapper", f"{mapper_name}.java"),
                        os.path.join(self.output_dir, "scf-loan-biz", "src", "main", "java", *self.base_package.split("."), "biz", "service", f"{service_name}.java"),
                        os.path.join(self.output_dir, "scf-loan-biz", "src", "main", "java", *self.base_package.split("."), "biz", "service", "impl", f"{service_name}Impl.java"),
                        os.path.join(self.output_dir, "scf-loan-common", "src", "main", "java", *self.base_package.split("."), "common", "dto", f"{dto_name}.java"),
                        os.path.join(self.output_dir, "scf-loan-web", "src", "main", "java", *self.base_package.split("."), "web", "controller", f"{entity_name.replace('Entity', 'Controller')}.java")
                    ]
                    
                    if generate_tests:
                        generated_files.append(
                            os.path.join(self.output_dir, "scf-loan-biz", "src", "test", "java", *self.base_package.split("."), "biz", "service", "impl", f"{service_name}Test.java")
                        )
                    
                    # 验证代码
                    if validate:
                        self.validate_code(generated_files)
                    
                    print("\nCode generation completed successfully!")
                except Exception as e:
                    print(f"Database connection failed: {e}")
                    print("Generating complete code based on default SQL statement...")
                    
                    # 提供t_scf_financing_order表的SQL语句
                    sql = """CREATE TABLE `t_scf_financing_order` ( 
   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID', 
   `uid` varchar(64) NOT NULL COMMENT '会员号', 
   `env` varchar(10) DEFAULT NULL COMMENT '环境标识', 
   `financing_order_id` varchar(32) DEFAULT NULL COMMENT '融资订单号', 
   `credit_contract_type` varchar(32) DEFAULT NULL COMMENT '授信协议类型', 
   `credit_contract_id` varchar(64) DEFAULT NULL COMMENT '成员ID', 
   `mer_trans_no` varchar(32) NOT NULL DEFAULT '' COMMENT '商户订单号', 
   `contract_no` varchar(64) NOT NULL DEFAULT '' COMMENT '供应链金融协议号', 
   `apply_status` varchar(32) NOT NULL DEFAULT '' COMMENT '审批状态 审批中(applying)，审批失败(refused)，审批成功(accept)', 
   `loan_confirm_status` varchar(32) NOT NULL DEFAULT '' COMMENT '放款申请中(loaning)，放款申请失败(loanFailed)，放款申请成功(loanSuccess)', 
   `repay_status` varchar(32) NOT NULL DEFAULT '' COMMENT '还款状态: 还款中(repaying),  逾期中(overDue), 已结清(clearUp)', 
   `loan_time` datetime DEFAULT NULL COMMENT '放款时间', 
   `clear_up_time` datetime DEFAULT NULL COMMENT '结清时间', 
   `loan_amount` bigint(20) NOT NULL COMMENT '放款金额', 
   `state_change_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单状态最近一次流转时间, 可根据该时间进行取数和告警', 
   `ext_info` text COMMENT '扩展信息, 可以用来暂存最新的还款计划', 
   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', 
   `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', 
   `created_by` varchar(64) DEFAULT NULL COMMENT '创建人', 
   `updated_by` varchar(64) DEFAULT NULL COMMENT '更新人', 
   `del_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志 0:有效 1:无效', 
   `revision` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '乐观锁版本号, 更新时需复核版本号', 
   `start_date` date DEFAULT NULL COMMENT '当期开始日期 yyyy-MM-dd', 
   `due_date` date DEFAULT NULL COMMENT '当期到期日期 yyyy-MM-dd', 
   PRIMARY KEY (`id`), 
   KEY `idx_financing_order` (`financing_order_id`) COMMENT '放款订单号索引', 
   KEY `idx_updated_at` (`updated_at`) COMMENT '更新时间索引', 
   KEY `idx_core_page` (`del_flag`,`apply_status`,`loan_confirm_status`,`repay_status`,`id`) COMMENT '分页查询索引' 
 ) ENGINE=InnoDB AUTO_INCREMENT=1978730110571778355 DEFAULT CHARSET=utf8mb4 COMMENT='融资订单表'"""
                    
                    # 保存DDL语句到文件
                    self.save_ddl(table_name, sql)
                    
                    # 从SQL语句解析表结构
                    table_schema = self.parse_table_schema_from_sql(table_name, sql)
                    
                    # 生成完整代码
                    entity_name = self.generate_entity(table_schema)
                    mapper_name = self.generate_mapper(entity_name, table_name)
                    service_name = self.generate_service(entity_name, table_name)
                    self.generate_service_impl(service_name, entity_name, mapper_name)
                    dto_name = self.generate_dto(entity_name, table_schema)
                    self.generate_controller(entity_name, service_name, dto_name)
                    
                    # 生成单元测试
                    if generate_tests:
                        self.generate_unit_test(entity_name, service_name)
                    
                    # 收集生成的文件
                    generated_files = [
                        os.path.join(self.output_dir, "scf-loan-dal", "src", "main", "java", *self.base_package.split("."), "dal", "entity", f"{entity_name}.java"),
                        os.path.join(self.output_dir, "scf-loan-dal", "src", "main", "java", *self.base_package.split("."), "dal", "mapper", f"{mapper_name}.java"),
                        os.path.join(self.output_dir, "scf-loan-biz", "src", "main", "java", *self.base_package.split("."), "biz", "service", f"{service_name}.java"),
                        os.path.join(self.output_dir, "scf-loan-biz", "src", "main", "java", *self.base_package.split("."), "biz", "service", "impl", f"{service_name}Impl.java"),
                        os.path.join(self.output_dir, "scf-loan-common", "src", "main", "java", *self.base_package.split("."), "common", "dto", f"{dto_name}.java"),
                        os.path.join(self.output_dir, "scf-loan-web", "src", "main", "java", *self.base_package.split("."), "web", "controller", f"{entity_name.replace('Entity', 'Controller')}.java")
                    ]
                    
                    if generate_tests:
                        generated_files.append(
                            os.path.join(self.output_dir, "scf-loan-biz", "src", "test", "java", *self.base_package.split("."), "biz", "service", "impl", f"{service_name}Test.java")
                        )
                    
                    # 验证代码
                    if validate:
                        self.validate_code(generated_files)
                    
                    print("\nCode generation completed successfully based on default SQL statement!")
        except Exception as e:
            print(f"Error generating code: {e}")
            import traceback
            traceback.print_exc()
        finally:
            self.disconnect()


def load_config():
    """加载配置文件"""
    config_path = os.path.join(os.path.dirname(__file__), "config.yml")
    if os.path.exists(config_path):
        with open(config_path, "r", encoding="utf-8") as f:
            return yaml.safe_load(f)
    return {}


@click.command()
@click.option('--db-url', default='mysql://localhost:3306/scf_loan', help='Database URL')
@click.option('--db-username', default='root', help='Database Username')
@click.option('--db-password', default='123456', help='Database Password')
@click.option('--table', default='t_scf_financing_order', help='Table Name')
@click.option('--ddl', default=None, help='DDL File Path')
@click.option('--output-dir', default='.', help='Output Directory')
@click.option('--validate', is_flag=True, default=False, help='Validate Generated Code')
@click.option('--generate-tests', is_flag=True, default=True, help='Generate Unit Tests')
def main(db_url, db_username, db_password, table, ddl, output_dir, validate, generate_tests):
    """数据库表代码生成工具"""
    # 加载配置
    config = load_config()
    
    # 使用配置文件中的值（如果存在）
    db_url = config.get("db_url", db_url)
    db_username = config.get("db_username", db_username)
    db_password = config.get("db_password", db_password)
    
    # 创建生成器
    generator = DatabaseGenerator(
        db_url=db_url,
        username=db_username,
        password=db_password,
        base_package="com.scf.loan",
        output_dir=output_dir
    )
    
    # 生成代码
    generator.generate_code(table_name=table, ddl_file=ddl, validate=validate, generate_tests=generate_tests)


if __name__ == "__main__":
    main()