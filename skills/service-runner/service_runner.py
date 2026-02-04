#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
服务编译运行工具
用于编译和运行scf-loan-web服务，并检测服务是否能正常启动运行
"""

import os
import subprocess
import time
import click
import urllib.request
import urllib.error


class ServiceRunner:
    def __init__(self, project_dir):
        """初始化服务运行器"""
        self.project_dir = project_dir
        self.web_module_dir = os.path.join(project_dir, "scf-loan-web")
        self.service_url = "http://localhost:8081/api/financing-order/health"
        self.service_process = None
    
    def check_maven(self):
        """检查Maven是否安装"""
        print("检查Maven是否安装...")
        try:
            # 执行 mvn -v 命令
            result = subprocess.run(
                ["mvn", "-v"],
                capture_output=True,
                text=True
            )
            
            if result.returncode == 0:
                print("Maven已安装！")
                return True
            else:
                print("Maven未安装或无法访问！")
                return False
        except FileNotFoundError:
            print("Maven未安装或未添加到系统PATH环境变量中！")
            return False
        except Exception as e:
            print(f"检查Maven时出现异常：{e}")
            return False
    
    def compile_service(self):
        """编译服务"""
        print("开始编译服务...")
        try:
            # 执行 mvn clean install 命令
            result = subprocess.run(
                ["mvn", "clean", "install", "-DskipTests"],
                cwd=self.project_dir,
                capture_output=True,
                text=True,
                timeout=300  # 设置5分钟超时
            )
            
            if result.returncode == 0:
                print("服务编译成功！")
                return True
            else:
                print("服务编译失败！")
                print("错误输出：")
                print(result.stderr)
                return False
        except subprocess.TimeoutExpired:
            print("编译超时，请检查网络和系统资源！")
            return False
        except Exception as e:
            print(f"编译过程中出现异常：{e}")
            return False
    
    def run_service(self):
        """运行服务"""
        print("开始运行服务...")
        try:
            # 执行 mvn spring-boot:run 命令
            self.service_process = subprocess.Popen(
                ["mvn", "spring-boot:run"],
                cwd=self.web_module_dir,
                stdout=subprocess.PIPE,
                stderr=subprocess.STDOUT,
                text=True
            )
            
            # 等待服务启动
            print("正在启动服务，请稍候...")
            time.sleep(10)  # 等待10秒，让服务有足够的时间启动
            
            return True
        except Exception as e:
            print(f"运行服务时出现异常：{e}")
            return False
    
    def check_service_health(self):
        """检查服务健康状态"""
        print("检查服务健康状态...")
        try:
            # 发送健康检查请求
            with urllib.request.urlopen(self.service_url, timeout=10) as response:
                status_code = response.getcode()
                response_text = response.read().decode('utf-8')
                
                if status_code == 200 and response_text == "OK":
                    print("服务健康检查通过！")
                    return True
                else:
                    print(f"服务健康检查失败！状态码：{status_code}，响应内容：{response_text}")
                    return False
        except urllib.error.URLError as e:
            print("无法连接到服务，请检查服务是否已启动！")
            return False
        except urllib.error.HTTPError as e:
            print(f"服务返回错误状态码：{e.code}")
            return False
        except Exception as e:
            print(f"检查服务健康状态时出现异常：{e}")
            return False
    
    def stop_service(self):
        """停止服务"""
        if self.service_process:
            print("停止服务...")
            try:
                # 尝试终止进程
                self.service_process.terminate()
                # 等待进程结束
                self.service_process.wait(timeout=10)
                print("服务已停止！")
            except subprocess.TimeoutExpired:
                # 如果终止失败，强制杀死进程
                self.service_process.kill()
                print("服务已强制停止！")
            except Exception as e:
                print(f"停止服务时出现异常：{e}")
            finally:
                self.service_process = None
    
    def run_full_process(self):
        """运行完整流程：编译 -> 运行 -> 检查健康状态"""
        try:
            # 检查Maven是否安装
            if not self.check_maven():
                return False
            
            # 编译服务
            if not self.compile_service():
                return False
            
            # 运行服务
            if not self.run_service():
                return False
            
            # 检查服务健康状态
            if not self.check_service_health():
                # 打印服务输出，以便调试
                if self.service_process:
                    output = self.service_process.stdout.read(2000)
                    print("服务输出：")
                    print(output)
                return False
            
            print("\n服务编译运行成功！")
            print(f"服务健康检查地址：{self.service_url}")
            return True
        finally:
            # 停止服务
            self.stop_service()


@click.command()
@click.option('--project-dir', default='.', help='项目根目录')
def main(project_dir):
    """服务编译运行工具"""
    # 转换为绝对路径
    project_dir = os.path.abspath(project_dir)
    print(f"项目根目录：{project_dir}")
    
    # 创建服务运行器
    runner = ServiceRunner(project_dir)
    
    # 运行完整流程
    success = runner.run_full_process()
    
    if success:
        print("\n任务执行成功！")
    else:
        print("\n任务执行失败！")
        exit(1)


if __name__ == "__main__":
    main()
