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
    
    def start_mysql(self):
        """启动MySQL服务"""
        print("\n步骤1: 启动MySQL服务")
        
        # 调用mysql-start skill启动MySQL服务
        command = ["python", "skills/mysql-start/mysql_start.py", "start"]
        return self.run_command(command)
    
    def generate_code(self):
        """生成数据库表代码"""
        print("\n步骤2: 生成数据库表代码")
        
        # 调用db-generate skill生成数据库表代码
        command = ["python", "skills/db-generate/db_generate.py", "--table", "t_scf_financing_order", "--generate-tests"]
        return self.run_command(command)
    
    def run_service(self):
        """执行服务编译和运行"""
        print("\n步骤3: 执行服务编译和运行")
        
        # 调用service-runner skill执行服务编译和运行
        command = ["python", "skills/service-runner/service_runner.py"]
        return self.run_command(command)
    
    def cleanup(self):
        """清理资源"""
        print("\n清理资源...")
        
        # 停止服务（service-runner会自动停止服务）
        print("服务已停止！")
        return True
    
    def run_full_process(self):
        """执行完整流程"""
        print("开始执行编排流程...")
        
        try:
            # 1. 启动MySQL服务
            mysql_started = self.start_mysql()
            if not mysql_started:
                print("启动MySQL服务失败，但将继续执行后续步骤...")
            else:
                # 等待MySQL服务完全启动
                time.sleep(5)
            
            # 2. 生成数据库表代码
            if not self.generate_code():
                print("生成数据库表代码失败，流程终止！")
                return False
            
            # 3. 执行服务编译和运行
            if not self.run_service():
                print("执行服务编译和运行失败，流程终止！")
                return False
            
            # 4. 清理资源
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
def run():
    """执行完整流程：启动MySQL → 生成表数据 → 执行runner"""
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    orchestrator = Orchestrator(project_dir)
    orchestrator.run_full_process()


@cli.command()
def start_mysql():
    """仅启动MySQL服务"""
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    orchestrator = Orchestrator(project_dir)
    orchestrator.start_mysql()


@cli.command()
def generate_code():
    """仅生成数据库表代码"""
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    orchestrator = Orchestrator(project_dir)
    orchestrator.generate_code()


@cli.command()
def run_service():
    """仅执行服务编译和运行"""
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    orchestrator = Orchestrator(project_dir)
    orchestrator.run_service()


@cli.command()
def cleanup():
    """清理资源，停止服务"""
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    orchestrator = Orchestrator(project_dir)
    orchestrator.cleanup()


if __name__ == "__main__":
    cli()
