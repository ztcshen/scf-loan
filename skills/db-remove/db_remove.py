#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
数据库表代码清理工具
根据数据库表名自动删除对应的实体类、Mapper、Service、Controller 等相关代码文件
"""

import os
import re
import click


class DatabaseRemover:
    def __init__(self, base_dir, base_package="com.scf.loan", force=False, dry_run=False):
        """初始化数据库代码清理工具"""
        self.base_dir = base_dir
        self.base_package = base_package
        self.force = force
        self.dry_run = dry_run
        self.files_to_remove = []
    
    def camel_case(self, s):
        """下划线转驼峰命名"""
        parts = s.split("_")
        return parts[0] + ''.join(x.title() for x in parts[1:])
    
    def pascal_case(self, s):
        """下划线转大驼峰命名"""
        parts = s.split("_")
        return ''.join(x.title() for x in parts)
    
    def get_class_names(self, table_name):
        """根据表名生成对应的类名"""
        # 处理表名，去除前缀，只使用核心部分
        parts = table_name.split("_")
        # 找到第一个不是前缀的部分（通常是业务名称）
        core_parts = []
        for part in parts:
            if part not in ["t", "scf"]:
                core_parts.append(part)
        # 如果没有找到核心部分，使用整个表名
        if not core_parts:
            core_parts = parts
        # 生成核心名称
        core_name = ''.join(x.title() for x in core_parts)
        
        # 生成实体类名
        entity_name = core_name + "Entity"
        
        # 生成其他类名
        mapper_name = entity_name.replace("Entity", "Mapper")
        service_name = entity_name.replace("Entity", "Service")
        service_impl_name = service_name + "Impl"
        dto_name = entity_name.replace("Entity", "DTO")
        controller_name = entity_name.replace("Entity", "Controller")
        test_name = service_name + "Test"
        
        return {
            "entity_name": entity_name,
            "mapper_name": mapper_name,
            "service_name": service_name,
            "service_impl_name": service_impl_name,
            "dto_name": dto_name,
            "controller_name": controller_name,
            "test_name": test_name
        }
    
    def get_file_paths(self, table_name, class_names):
        """根据表名和类名生成对应的文件路径"""
        # 生成包路径
        package_path = os.path.join(*self.base_package.split("."))
        
        # 生成文件路径
        file_paths = []
        
        # 实体类文件
        entity_path = os.path.join(
            self.base_dir,
            "scf-loan-dal",
            "src",
            "main",
            "java",
            package_path,
            "dal",
            "entity",
            f"{class_names['entity_name']}.java"
        )
        file_paths.append(entity_path)
        
        # Mapper接口文件
        mapper_path = os.path.join(
            self.base_dir,
            "scf-loan-dal",
            "src",
            "main",
            "java",
            package_path,
            "dal",
            "mapper",
            f"{class_names['mapper_name']}.java"
        )
        file_paths.append(mapper_path)
        
        # Mapper XML文件
        mapper_xml_path = os.path.join(
            self.base_dir,
            "scf-loan-dal",
            "src",
            "main",
            "resources",
            "mapper",
            f"{class_names['mapper_name']}.xml"
        )
        file_paths.append(mapper_xml_path)
        
        # Service接口文件
        service_path = os.path.join(
            self.base_dir,
            "scf-loan-biz",
            "src",
            "main",
            "java",
            package_path,
            "biz",
            "service",
            f"{class_names['service_name']}.java"
        )
        file_paths.append(service_path)
        
        # Service实现类文件
        service_impl_path = os.path.join(
            self.base_dir,
            "scf-loan-biz",
            "src",
            "main",
            "java",
            package_path,
            "biz",
            "service",
            "impl",
            f"{class_names['service_impl_name']}.java"
        )
        file_paths.append(service_impl_path)
        
        # DTO文件
        dto_path = os.path.join(
            self.base_dir,
            "scf-loan-common",
            "src",
            "main",
            "java",
            package_path,
            "common",
            "dto",
            f"{class_names['dto_name']}.java"
        )
        file_paths.append(dto_path)
        
        # Converter文件（位于biz模块）
        converter_name = class_names['entity_name'].replace("Entity", "Converter")
        converter_path_biz = os.path.join(
            self.base_dir,
            "scf-loan-biz",
            "src",
            "main",
            "java",
            package_path,
            "biz",
            "convert",
            f"{converter_name}.java"
        )
        file_paths.append(converter_path_biz)
        # 兼容旧位置（common模块）
        converter_path_common = os.path.join(
            self.base_dir,
            "scf-loan-common",
            "src",
            "main",
            "java",
            package_path,
            "common",
            "convert",
            f"{converter_name}.java"
        )
        file_paths.append(converter_path_common)
        
        # Controller文件
        controller_path = os.path.join(
            self.base_dir,
            "scf-loan-web",
            "src",
            "main",
            "java",
            package_path,
            "web",
            "controller",
            f"{class_names['controller_name']}.java"
        )
        file_paths.append(controller_path)
        
        # 单元测试文件
        test_path = os.path.join(
            self.base_dir,
            "scf-loan-biz",
            "src",
            "test",
            "java",
            package_path,
            "biz",
            "service",
            "impl",
            f"{class_names['test_name']}.java"
        )
        file_paths.append(test_path)
        
        return file_paths
    
    def find_files(self, table_name):
        """查找要删除的文件"""
        class_names = self.get_class_names(table_name)
        file_paths = self.get_file_paths(table_name, class_names)
        
        # 收集存在的文件
        existing_files = []
        for file_path in file_paths:
            if os.path.exists(file_path):
                existing_files.append(file_path)
        
        self.files_to_remove = existing_files
        return existing_files
    
    def remove_files(self, table_name):
        """删除文件"""
        existing_files = self.find_files(table_name)
        
        if not existing_files:
            print(f"No files found for table: {table_name}")
            return
        
        # 预览删除
        if self.dry_run:
            print(f"\nDry run mode: Files to remove for table {table_name}:")
            for file_path in existing_files:
                print(f"  - {os.path.relpath(file_path, self.base_dir)}")
            print(f"\nTotal files to remove: {len(existing_files)}")
            return
        
        # 确认删除
        if not self.force:
            print(f"\nFiles to remove for table {table_name}:")
            for file_path in existing_files:
                print(f"  - {os.path.relpath(file_path, self.base_dir)}")
            
            confirm = input(f"\nAre you sure you want to remove {len(existing_files)} files? (y/N): ")
            if confirm.lower() != 'y':
                print("Aborted.")
                return
        
        # 执行删除
        removed_count = 0
        failed_count = 0
        
        print(f"\nRemoving files for table {table_name}:")
        for file_path in existing_files:
            try:
                os.remove(file_path)
                print(f"  ✓ Removed: {os.path.relpath(file_path, self.base_dir)}")
                removed_count += 1
            except Exception as e:
                print(f"  ✗ Failed to remove: {os.path.relpath(file_path, self.base_dir)} - {e}")
                failed_count += 1
        
        print(f"\nRemoval completed:")
        print(f"  Removed: {removed_count} files")
        print(f"  Failed: {failed_count} files")
    
    def remove_tables(self, tables):
        """删除多个表的代码文件"""
        for table in tables:
            print(f"\nProcessing table: {table}")
            self.remove_files(table)


@click.command()
@click.option('--base-dir', default='.', help='Project Base Directory')
@click.option('--tables', default='t_scf_financing_order', help='Table Name(s) (comma-separated)')
@click.option('--force', is_flag=True, default=False, help='Force Removal')
@click.option('--dry-run', is_flag=True, default=False, help='Dry Run (Preview Only)')
def main(base_dir, tables, force, dry_run):
    """数据库表代码清理工具"""
    # 解析表名列表
    tables = [table.strip() for table in tables.split(",")]
    
    # 创建清理工具
    remover = DatabaseRemover(
        base_dir=base_dir,
        force=force,
        dry_run=dry_run
    )
    
    # 执行删除
    remover.remove_tables(tables)


if __name__ == "__main__":
    main()
