# 服务编译运行工具 (service-runner)

## 功能描述

此工具用于编译和运行scf-loan-web服务，并可选检测服务是否能正常启动运行。它会执行以下操作：

1. 编译整个项目（执行 `mvn clean install`）
2. 运行scf-loan-web服务（执行 `mvn spring-boot:run`）
3. （可选）检测服务是否能正常启动（访问健康检查接口）
4. 停止服务

## 安装依赖

此工具依赖于以下Python库：

- `click`：用于命令行参数解析
- `requests`：用于发送HTTP请求，检测服务健康状态

可以通过以下命令安装依赖：

```bash
pip install click requests
```

## 使用方法

### 基本用法

在项目根目录下执行：

```bash
python skills/service-runner/service_runner.py --skip-health
```

### 指定项目目录

如果需要在其他目录执行，可以通过 `--project-dir` 参数指定项目根目录：

```bash
python skills/service-runner/service_runner.py --project-dir /path/to/scf-loan
```

## 执行流程

1. **编译服务**：执行 `mvn clean install -DskipTests` 命令，编译整个项目
2. **运行服务**：执行 `mvn spring-boot:run` 命令，运行scf-loan-web服务
3. **等待服务启动**：等待10秒，让服务有足够的时间启动
4. **（可选）检查健康状态**：访问 `SCF_SERVICE_URL` 或 `--service-url` 指定的接口；默认 `--skip-health` 启用，跳过检查
5. **停止服务**：无论成功与否，都会停止服务进程

## 输出示例

### 成功示例

```
项目根目录：D:\IdeaProjects\scf-loan
开始编译服务...
服务编译成功！
开始运行服务...
正在启动服务，请稍候...
检查服务健康状态...
服务健康检查通过！

服务编译运行成功！
服务健康检查地址：http://localhost:8081/api/financing-order/health
停止服务...
服务已停止！

任务执行成功！
```

### 失败示例

```
项目根目录：D:\IdeaProjects\scf-loan
开始编译服务...
服务编译成功！
开始运行服务...
正在启动服务，请稍候...
检查服务健康状态...
无法连接到服务，请检查服务是否已启动！
服务输出：
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------< com.scf:scf-loan-web >------------------------
[INFO] Building scf-loan-web 1.0.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
...

停止服务...
服务已停止！

任务执行失败！
```

## 注意事项

1. **依赖要求**：确保已安装Maven和Java环境
2. **网络要求**：编译过程中需要下载依赖，确保网络连接正常
3. **端口要求**：服务默认使用8081端口，确保此端口未被占用
4. **超时设置**：编译过程设置了5分钟超时，运行过程设置了10秒超时
5. **健康检查**：若启用，需配置健康检查端点并返回 "OK"；默认跳过

## 故障排除

如果服务编译运行失败，可以查看以下信息：

1. **编译错误**：检查Maven依赖是否正确，网络是否正常
2. **启动错误**：检查服务配置是否正确，端口是否被占用
3. **健康检查失败**：检查服务是否正常启动，健康检查接口是否存在

可以通过查看工具输出的详细信息来定位问题。
