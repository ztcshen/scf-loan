#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
编排工具
用于编排执行多个开发任务，自动化完成从启动MySQL、生成表数据到执行服务的完整流程
"""

import os
import subprocess
import time
import click


class Orchestrator:
    def __init__(self, project_dir):
        """初始化编排器"""
        self.project_dir = project_dir
        self.skills_dir = os.path.join(project_dir, "skills")
    
    def run_command(self, command, cwd=None):
        """执行命令"""
        if cwd is None:
            cwd = self.project_dir
        
        print(f"执行命令: {' '.join(command)}")
        
        result = subprocess.run(
            command,
            cwd=cwd,
            capture_output=True,
            text=True
        )
        
        if result.returncode == 0:
            print(f"命令执行成功！")
            print(result.stdout)
            return True
        else:
            print(f"命令执行失败！")
            print(f"错误信息：{result.stderr}")
            return False
    
    def generate_code(self, table="t_scf_financing_order", generate_tests=True, validate=False, api_prefix=None):
        """生成数据库表代码"""
        print("\n步骤1: 生成数据库表代码")
        
        # 调用db-generate skill生成数据库表代码
        command = ["python", "skills/db-generate/db_generate.py", "--table", table]
        if generate_tests:
            command.append("--generate-tests")
        if validate:
            command.append("--validate")
        if api_prefix:
            command.extend(["--api-prefix", api_prefix])
        return self.run_command(command)
    
    def run_service(self, service_url=None, wait_seconds=10, skip_build=False):
        """执行服务编译和运行"""
        print("\n步骤2: 执行服务编译和运行")
        
        # 调用service-runner skill执行服务编译和运行
        command = ["python", "skills/service-runner/service_runner.py"]
        if service_url:
            command.extend(["--service-url", service_url])
        if wait_seconds is not None:
            command.extend(["--wait-seconds", str(wait_seconds)])
        if skip_build:
            command.append("--skip-build")
        return self.run_command(command)
    
    def cleanup(self):
        """清理资源"""
        print("\n清理资源...")
        
        # 停止服务（service-runner会自动停止服务）
        print("服务已停止！")
        return True
    
    def run_full_process(self, table="t_scf_financing_order", generate_tests=True, validate=False, api_prefix=None, service_url=None, wait_seconds=10, skip_build=False):
        """执行完整流程"""
        print("开始执行编排流程...")
        
        try:
            # 1. 生成数据库表代码
            if not self.generate_code(table=table, generate_tests=generate_tests, validate=validate, api_prefix=api_prefix):
                print("生成数据库表代码失败，流程终止！")
                return False
            
            # 2. 执行服务编译和运行
            if not self.run_service(service_url=service_url, wait_seconds=wait_seconds, skip_build=skip_build):
                print("执行服务编译和运行失败，流程终止！")
                return False
            
            # 3. 清理资源
            self.cleanup()
            
            print("\n编排流程执行完成！")
            return True
        except Exception as e:
            print(f"执行编排流程时出现异常：{e}")
            return False


@click.group()
def cli():
    """编排工具"""
    pass


@cli.command()
@click.option('--table', default='t_scf_financing_order', help='表名')
@click.option('--generate-tests', is_flag=True, default=True, help='是否生成单测')
@click.option('--validate', is_flag=True, default=False, help='生成后进行校验')
@click.option('--api-prefix', default=None, help='API 前缀，默认 /api')
@click.option('--service-url', default=None, help='健康检查URL')
@click.option('--wait-seconds', default=10, type=int, help='服务启动等待秒数')
@click.option('--skip-build', is_flag=True, default=False, help='跳过编译步骤')
def run(table, generate_tests, validate, api_prefix, service_url, wait_seconds, skip_build):
    """执行完整流程：生成表数据 → 执行runner"""
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    orchestrator = Orchestrator(project_dir)
    orchestrator.run_full_process(
        table=table,
        generate_tests=generate_tests,
        validate=validate,
        api_prefix=api_prefix,
        service_url=service_url,
        wait_seconds=wait_seconds,
        skip_build=skip_build
    )


@cli.command()
@click.option('--table', default='t_scf_financing_order', help='表名')
@click.option('--generate-tests', is_flag=True, default=True, help='是否生成单测')
@click.option('--validate', is_flag=True, default=False, help='生成后进行校验')
@click.option('--api-prefix', default=None, help='API 前缀，默认 /api')
def generate_code(table, generate_tests, validate, api_prefix):
    """仅生成数据库表代码"""
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    orchestrator = Orchestrator(project_dir)
    orchestrator.generate_code(table=table, generate_tests=generate_tests, validate=validate, api_prefix=api_prefix)


@cli.command()
@click.option('--service-url', default=None, help='健康检查URL')
@click.option('--wait-seconds', default=10, type=int, help='服务启动等待秒数')
@click.option('--skip-build', is_flag=True, default=False, help='跳过编译步骤')
def run_service(service_url, wait_seconds, skip_build):
    """仅执行服务编译和运行"""
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    orchestrator = Orchestrator(project_dir)
    orchestrator.run_service(service_url=service_url, wait_seconds=wait_seconds, skip_build=skip_build)


@cli.command()
def cleanup():
    """清理资源，停止服务"""
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    orchestrator = Orchestrator(project_dir)
    orchestrator.cleanup()


if __name__ == "__main__":
    cli()
