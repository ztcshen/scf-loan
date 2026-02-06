#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
数据库表代码清理工具
用于删除由db_generate.py生成的文件
"""

import os
import re
import click


def remove_files(base_dir, table_name):
    """删除生成的文件"""
    # 转换表名为驼峰命名
    def pascal_case(s):
        parts = s.split("_")
        # 去除常见的表名前缀，如t_、tbl_等
        if parts and (parts[0] == 't' or parts[0] == 'tbl'):
            parts = parts[1:]
        # 去除模块前缀，如scf_
        if parts and parts[0] == 'scf':
            parts = parts[1:]
        return ''.join(x.title() for x in parts)
    
    entity_name = pascal_case(table_name) + "Entity"
    service_name = entity_name.replace("Entity", "Service")
    mapper_name = entity_name.replace("Entity", "Mapper")
    dto_name = entity_name.replace("Entity", "DTO")
    controller_name = entity_name.replace("Entity", "Controller")
    test_name = service_name + "Test"
    
    # 定义要删除的文件路径
    files_to_remove = [
        # 实体类
        os.path.join(base_dir, "scf-loan-dal", "src", "main", "java", "com", "scf", "loan", "dal", "entity", f"{entity_name}.java"),
        # Mapper接口
        os.path.join(base_dir, "scf-loan-dal", "src", "main", "java", "com", "scf", "loan", "dal", "mapper", f"{mapper_name}.java"),
        # Service接口
        os.path.join(base_dir, "scf-loan-biz", "src", "main", "java", "com", "scf", "loan", "biz", "service", f"{service_name}.java"),
        # Service实现类
        os.path.join(base_dir, "scf-loan-biz", "src", "main", "java", "com", "scf", "loan", "biz", "service", "impl", f"{service_name}Impl.java"),
        # DTO类
        os.path.join(base_dir, "scf-loan-common", "src", "main", "java", "com", "scf", "loan", "common", "dto", f"{dto_name}.java"),
        # Controller类
        os.path.join(base_dir, "scf-loan-web", "src", "main", "java", "com", "scf", "loan", "web", "controller", f"{controller_name}.java"),
        # 单元测试类
        os.path.join(base_dir, "scf-loan-biz", "src", "test", "java", "com", "scf", "loan", "biz", "service", "impl", f"{test_name}.java")
    ]
    
    # 执行删除操作
    for file_path in files_to_remove:
        if os.path.exists(file_path):
            os.remove(file_path)
            print(f"已删除文件: {file_path}")
        else:
            print(f"文件不存在: {file_path}")


@click.command()
@click.option('--table', default='t_scf_financing_order', help='Table Name')
@click.option('--output-dir', default='.', help='Output Directory')
def main(table, output_dir):
    """数据库表代码清理工具"""
    remove_files(output_dir, table)
    print("\n文件清理完成！")


if __name__ == "__main__":
    main()
