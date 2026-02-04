#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
MySQL 本地启动工具
用于在本地启动MySQL服务，方便开发和测试使用
"""

import os
import subprocess
import time
import click
import platform


class MySQLStarter:
    def __init__(self):
        """初始化MySQL启动器"""
        self.mysql_config = {
            "host": "10.0.20.108",
            "port": "3306",
            "user": "baofoo",
            "password": "nx8o9J%3Pk5DGOMuHgL$",
            "database": "scf_loan"
        }
    
    def check_mysql_installed(self):
        """检查MySQL是否安装"""
        print("检查MySQL是否安装...")
        try:
            # 执行 mysql --version 命令
            result = subprocess.run(
                ["mysql", "--version"],
                capture_output=True,
                text=True
            )
            
            if result.returncode == 0:
                print("MySQL已安装！")
                return True
            else:
                print("MySQL未安装！")
                return False
        except FileNotFoundError:
            print("MySQL未安装！")
            return False
        except Exception as e:
            print(f"检查MySQL时出现异常：{e}")
            return False
    
    def install_mysql(self):
        """安装MySQL"""
        print("开始安装MySQL...")
        
        # 检查操作系统类型
        os_type = os.name
        print(f"检测到操作系统类型：{os_type}")
        
        if os_type == "nt":  # Windows
            print("Windows系统安装MySQL：")
            print("\n方法1：使用MySQL Installer自动安装（推荐）")
            print("1. 下载MySQL Installer：https://dev.mysql.com/downloads/installer/")
            print("2. 运行安装程序，选择'Custom'安装类型")
            print("3. 选择MySQL Server和MySQL Workbench（可选）")
            print("4. 设置MySQL root用户密码为：123456")
            print("5. 选择'Use Legacy Authentication Method'")
            print("6. 完成安装并启动MySQL服务")
            
            print("\n方法2：使用Chocolatey包管理器自动安装")
            print("1. 安装Chocolatey：https://chocolatey.org/install")
            print("2. 以管理员身份运行PowerShell")
            print("3. 执行命令：choco install mysql -y")
            print("4. 执行命令：mysqld --initialize-insecure")
            print("5. 执行命令：mysqld --install")
            print("6. 执行命令：net start mysql")
            print("7. 执行命令：mysqladmin -u root password '123456'")
        elif os_type == "posix":  # macOS/Linux
            print("macOS/Linux系统安装MySQL：")
            
            if platform.system() == "Darwin":  # macOS
                print("\n方法1：使用Homebrew包管理器自动安装（推荐）")
                print('1. 安装Homebrew：/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"')
                print("2. 执行命令：brew install mysql")
                print("3. 执行命令：brew services start mysql")
                print("4. 执行命令：mysql_secure_installation")
                print("5. 设置MySQL root用户密码为：123456")
            else:  # Linux
                print("\n方法1：使用apt包管理器自动安装（Ubuntu/Debian）")
                print("1. 执行命令：sudo apt update")
                print("2. 执行命令：sudo apt install mysql-server -y")
                print("3. 执行命令：sudo systemctl start mysql")
                print("4. 执行命令：sudo mysql_secure_installation")
                print("5. 设置MySQL root用户密码为：123456")
                
                print("\n方法2：使用yum包管理器自动安装（CentOS/RHEL）")
                print("1. 执行命令：sudo yum update")
                print("2. 执行命令：sudo yum install mysql-server -y")
                print("3. 执行命令：sudo systemctl start mysqld")
                print("4. 执行命令：sudo mysql_secure_installation")
                print("5. 设置MySQL root用户密码为：123456")
        
        print("\n通用安装步骤：")
        print("1. 下载MySQL安装包：https://dev.mysql.com/downloads/mysql/")
        print("2. 运行安装程序，按照向导完成安装")
        print("3. 选择'Custom'安装类型")
        print("4. 选择需要安装的组件")
        print("5. 设置MySQL root用户密码为：123456")
        print("6. 选择'Use Legacy Authentication Method'")
        print("7. 完成安装并启动MySQL服务")
        
        print("\n安装完成后，请执行以下命令验证MySQL是否安装成功：")
        print("mysql --version")
        print("\n然后重新运行此命令启动MySQL服务。")
        
        return False
    
    def check_service_status(self):
        """检查MySQL服务状态"""
        print("检查MySQL服务状态...")
        try:
            # 执行 mysqladmin ping 命令
            result = subprocess.run(
                ["mysqladmin", "ping", "-h", self.mysql_config["host"], "-P", self.mysql_config["port"]],
                capture_output=True,
                text=True
            )
            
            if result.returncode == 0:
                print("MySQL服务正在运行！")
                return True
            else:
                print("MySQL服务未启动！")
                return False
        except Exception as e:
            print("MySQL服务未启动！")
            return False
    
    def start_service(self):
        """启动MySQL服务"""
        # 检查MySQL是否安装
        if not self.check_mysql_installed():
            # 调用安装指南
            return self.install_mysql()
        
        # 检查服务状态
        if self.check_service_status():
            print("MySQL服务已启动，无需重复操作！")
            return True
        
        print("MySQL服务未启动，正在启动...")
        
        try:
            # 根据操作系统执行不同的启动命令
            if os.name == "nt":  # Windows
                # 执行 net start mysql 命令
                result = subprocess.run(
                    ["net", "start", "mysql"],
                    capture_output=True,
                    text=True
                )
            else:  # macOS/Linux
                # 执行 sudo service mysql start 命令
                result = subprocess.run(
                    ["sudo", "service", "mysql", "start"],
                    capture_output=True,
                    text=True
                )
            
            if result.returncode == 0:
                print("MySQL服务启动成功！")
                # 等待服务启动完成
                time.sleep(3)
                # 验证服务是否正常运行
                return self.verify_service()
            else:
                print("MySQL服务启动失败！")
                print(f"错误信息：{result.stderr}")
                return False
        except Exception as e:
            print(f"启动MySQL服务时出现异常：{e}")
            return False
    
    def stop_service(self):
        """停止MySQL服务"""
        # 检查MySQL是否安装
        if not self.check_mysql_installed():
            # 调用安装指南
            return self.install_mysql()
        
        # 检查服务状态
        if not self.check_service_status():
            print("MySQL服务未启动，无需停止！")
            return True
        
        print("MySQL服务已启动，正在停止...")
        
        try:
            # 根据操作系统执行不同的停止命令
            if os.name == "nt":  # Windows
                # 执行 net stop mysql 命令
                result = subprocess.run(
                    ["net", "stop", "mysql"],
                    capture_output=True,
                    text=True
                )
            else:  # macOS/Linux
                # 执行 sudo service mysql stop 命令
                result = subprocess.run(
                    ["sudo", "service", "mysql", "stop"],
                    capture_output=True,
                    text=True
                )
            
            if result.returncode == 0:
                print("MySQL服务停止成功！")
                # 等待服务停止完成
                time.sleep(3)
                # 验证服务是否已停止
                return not self.check_service_status()
            else:
                print("MySQL服务停止失败！")
                print(f"错误信息：{result.stderr}")
                return False
        except Exception as e:
            print(f"停止MySQL服务时出现异常：{e}")
            return False
    
    def verify_service(self):
        """验证MySQL服务是否正常运行"""
        print("验证MySQL服务是否正常运行...")
        # 尝试连接MySQL
        try:
            result = subprocess.run(
                ["mysql", "-h", self.mysql_config["host"], "-P", self.mysql_config["port"], "-u", self.mysql_config["user"], "-p" + self.mysql_config["password"], "-e", "SELECT 1"],
                capture_output=True,
                text=True
            )
            
            if result.returncode == 0:
                print("MySQL服务运行正常！")
                return True
            else:
                print("MySQL服务运行异常！")
                return False
        except Exception as e:
            print(f"验证MySQL服务时出现异常：{e}")
            return False
    
    def show_connection_info(self):
        """显示MySQL连接信息"""
        print("MySQL连接信息：")
        print(f"- 主机：{self.mysql_config['host']}")
        print(f"- 端口：{self.mysql_config['port']}")
        print(f"- 用户名：{self.mysql_config['user']}")
        print(f"- 密码：{self.mysql_config['password']}")
        print(f"- 数据库：{self.mysql_config['database']}")
    
    def start(self):
        """启动MySQL服务并显示连接信息"""
        if self.start_service():
            self.show_connection_info()
    
    def stop(self):
        """停止MySQL服务"""
        self.stop_service()
    
    def status(self):
        """检查MySQL服务状态并显示连接信息"""
        status = self.check_service_status()
        if status:
            self.show_connection_info()
    
    def info(self):
        """显示MySQL连接信息"""
        self.show_connection_info()
    
    def install(self):
        """显示MySQL安装指南"""
        self.install_mysql()


@click.group()
def cli():
    """MySQL 本地启动工具"""
    pass


@cli.command()
def start():
    """启动MySQL服务"""
    starter = MySQLStarter()
    starter.start()


@cli.command()
def stop():
    """停止MySQL服务"""
    starter = MySQLStarter()
    starter.stop()


@cli.command()
def status():
    """检查MySQL服务状态"""
    starter = MySQLStarter()
    starter.status()


@cli.command()
def info():
    """查看MySQL连接信息"""
    starter = MySQLStarter()
    starter.info()


@cli.command()
def install():
    """显示MySQL安装指南"""
    starter = MySQLStarter()
    starter.install()


if __name__ == "__main__":
    cli()
