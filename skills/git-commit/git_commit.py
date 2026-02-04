#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Git 提交代码脚本
用于自动化执行 Git 代码提交流程
"""

import os
import subprocess
import yaml
import click
import re


class GitCommit:
    def __init__(self, project_dir, message, branch=None, skip_tests=False, skip_build=False, verbose=False):
        """初始化"""
        self.project_dir = project_dir
        self.message = message
        self.branch = branch
        self.skip_tests = skip_tests
        self.skip_build = skip_build
        self.verbose = verbose
        self.config = self.load_config()
        
    def load_config(self):
        """加载配置文件"""
        config_path = os.path.join(os.path.dirname(__file__), "config.yml")
        if os.path.exists(config_path):
            with open(config_path, "r", encoding="utf-8") as f:
                return yaml.safe_load(f)
        return {}
    
    def run_command(self, command, cwd=None, capture_output=True):
        """执行命令"""
        if cwd is None:
            cwd = self.project_dir
        
        if self.verbose:
            print(f"执行命令: {command}")
        
        result = subprocess.run(
            command, 
            shell=True, 
            cwd=cwd, 
            capture_output=capture_output, 
            text=True
        )
        
        if self.verbose:
            if result.stdout:
                print(f"命令输出: {result.stdout}")
            if result.stderr:
                print(f"命令错误: {result.stderr}")
        
        return result
    
    def check_git_status(self):
        """检查Git状态"""
        print("检查Git状态...")
        
        # 检查是否初始化了Git仓库
        result = self.run_command("git rev-parse --is-inside-work-tree")
        if result.returncode != 0:
            raise Exception("项目未初始化Git仓库，请先执行 git init")
        
        # 检查是否有改动
        result = self.run_command("git status --porcelain")
        if not result.stdout.strip():
            raise Exception("没有改动的文件，无需提交")
        
        # 获取改动文件列表
        changed_files = []
        for line in result.stdout.strip().split('\n'):
            if line.strip():
                status, file_path = line.strip().split(' ', 1)
                changed_files.append({
                    "status": status,
                    "path": file_path
                })
        
        print(f"发现 {len(changed_files)} 个改动文件")
        if self.verbose:
            for file in changed_files:
                print(f"  - {file['status']}: {file['path']}")
        
        return changed_files
    
    def run_tests(self):
        """执行单元测试"""
        if self.skip_tests:
            print("跳过单元测试")
            return
        
        print("执行单元测试...")
        
        # 执行Maven测试
        result = self.run_command("mvn test")
        if result.returncode != 0:
            raise Exception("单元测试失败，请修复测试后再提交")
        
        print("单元测试通过")
    
    def build_project(self):
        """编译项目"""
        if self.skip_build:
            print("跳过编译验证")
            return
        
        print("验证项目编译...")
        
        # 执行Maven编译
        result = self.run_command("mvn compile")
        if result.returncode != 0:
            raise Exception("项目编译失败，请修复编译错误后再提交")
        
        print("项目编译成功")
    
    def generate_change_summary(self, changed_files):
        """生成改动点总结"""
        print("分析改动点...")
        
        summary = {
            "total": len(changed_files),
            "added": 0,
            "modified": 0,
            "deleted": 0,
            "others": 0,
            "files": []
        }
        
        # 分析改动类型
        for file in changed_files:
            status = file['status']
            if status.startswith('A'):
                summary['added'] += 1
            elif status.startswith('M'):
                summary['modified'] += 1
            elif status.startswith('D'):
                summary['deleted'] += 1
            else:
                summary['others'] += 1
            
            summary['files'].append(file['path'])
        
        # 生成总结文本
        summary_text = f"\n改动点总结：\n"
        summary_text += f"- 总改动文件数：{summary['total']}\n"
        summary_text += f"- 新增文件数：{summary['added']}\n"
        summary_text += f"- 修改文件数：{summary['modified']}\n"
        summary_text += f"- 删除文件数：{summary['deleted']}\n"
        summary_text += f"- 其他改动数：{summary['others']}\n"
        
        if summary['files']:
            summary_text += "\n改动文件列表：\n"
            for file in summary['files'][:10]:  # 只显示前10个文件
                summary_text += f"  - {file}\n"
            if len(summary['files']) > 10:
                summary_text += f"  ... 等 {len(summary['files']) - 10} 个文件\n"
        
        print("改动点分析完成")
        if self.verbose:
            print(summary_text)
        
        return summary_text
    
    def commit_changes(self, summary_text):
        """执行Git提交"""
        print("准备提交代码...")
        
        # 构建提交信息
        commit_message = f"{self.message}{summary_text}"
        
        if self.verbose:
            print(f"提交信息: {commit_message}")
        
        # 添加所有改动文件
        result = self.run_command("git add .")
        if result.returncode != 0:
            raise Exception("Git add 操作失败")
        
        # 执行提交
        result = self.run_command(f"git commit -m \"{commit_message}\"")
        if result.returncode != 0:
            raise Exception("Git commit 操作失败")
        
        # 获取提交哈希
        result = self.run_command("git rev-parse HEAD")
        commit_hash = result.stdout.strip()
        
        print(f"代码提交成功！")
        print(f"提交哈希: {commit_hash}")
        
        return commit_hash
    
    def execute(self):
        """执行完整流程"""
        try:
            print("开始执行Git提交流程...")
            
            # 1. 检查Git状态
            changed_files = self.check_git_status()
            
            # 2. 执行单元测试
            self.run_tests()
            
            # 3. 编译项目
            self.build_project()
            
            # 4. 生成改动点总结
            summary_text = self.generate_change_summary(changed_files)
            
            # 5. 执行提交
            commit_hash = self.commit_changes(summary_text)
            
            print("\nGit提交流程执行完成！")
            print(f"提交结果: 成功")
            print(f"提交哈希: {commit_hash}")
            
            return {
                "success": True,
                "commit_hash": commit_hash,
                "changed_files_count": len(changed_files)
            }
            
        except Exception as e:
            print(f"\nGit提交流程执行失败: {e}")
            return {
                "success": False,
                "error": str(e)
            }


@click.command()
@click.option('--message', '-m', required=True, help='提交信息')
@click.option('--branch', '-b', default=None, help='分支名称')
@click.option('--skip-tests', is_flag=True, default=False, help='跳过单元测试')
@click.option('--skip-build', is_flag=True, default=False, help='跳过编译验证')
@click.option('--verbose', is_flag=True, default=False, help='详细输出日志')
def main(message, branch, skip_tests, skip_build, verbose):
    """Git 提交代码脚本"""
    # 确定项目目录
    project_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    
    print(f"项目目录: {project_dir}")
    print(f"提交信息: {message}")
    
    # 创建GitCommit实例
    git_commit = GitCommit(
        project_dir=project_dir,
        message=message,
        branch=branch,
        skip_tests=skip_tests,
        skip_build=skip_build,
        verbose=verbose
    )
    
    # 执行提交流程
    result = git_commit.execute()
    
    if result['success']:
        exit(0)
    else:
        exit(1)


if __name__ == "__main__":
    main()
