# MySQL 本地启动工具 (mysql-start)

## 功能描述

此工具用于在本地启动MySQL服务，方便开发和测试使用。它可以：

1. 检查本地是否安装了MySQL
2. 启动MySQL服务
3. 停止MySQL服务
4. 检查MySQL服务状态
5. 提供MySQL连接信息

## 安装依赖

此工具依赖于以下Python库：

- `click`：用于命令行参数解析

可以通过以下命令安装依赖：

```bash
pip install click
```

## 使用方法

### 基本用法

在项目根目录下执行：

```bash
python skills/mysql-start/mysql_start.py start
```

### 查看帮助信息

```bash
python skills/mysql-start/mysql_start.py --help
```

### 命令列表

| 命令 | 描述 |
|------|------|
| `start` | 启动MySQL服务（如果未安装，会显示安装指南） |
| `stop` | 停止MySQL服务（如果未安装，会显示安装指南） |
| `status` | 检查MySQL服务状态（如果未安装，会显示安装指南） |
| `info` | 查看MySQL连接信息 |
| `install` | 显示MySQL安装指南 |

## 执行流程

### 启动MySQL服务

1. 检查本地是否安装了MySQL
2. 检查MySQL服务是否已启动
3. 如果未启动，执行启动命令
4. 等待服务启动完成
5. 验证服务是否正常运行

### 停止MySQL服务

1. 检查MySQL服务是否已启动
2. 如果已启动，执行停止命令
3. 等待服务停止完成
4. 验证服务是否已停止

### 检查MySQL服务状态

1. 执行MySQL状态检查命令
2. 显示服务状态信息

### 查看MySQL连接信息

1. 显示MySQL连接的主机、端口、用户名和密码信息

## 输出示例

### 启动MySQL服务

```
检查MySQL是否安装...
MySQL已安装！
检查MySQL服务状态...
MySQL服务未启动，正在启动...
MySQL服务启动成功！
验证MySQL服务是否正常运行...
MySQL服务运行正常！
MySQL连接信息：
- 主机：localhost
- 端口：3306
- 用户名：root
- 密码：123456
- 数据库：scf_loan
```

### 停止MySQL服务

```
检查MySQL服务状态...
MySQL服务已启动，正在停止...
MySQL服务停止成功！
验证MySQL服务是否已停止...
MySQL服务已停止！
```

### 检查MySQL服务状态

```
检查MySQL服务状态...
MySQL服务正在运行！
MySQL连接信息：
- 主机：localhost
- 端口：3306
- 用户名：root
- 密码：123456
- 数据库：scf_loan
```

### 查看MySQL连接信息

```
MySQL连接信息：
- 主机：localhost
- 端口：3306
- 用户名：root
- 密码：123456
- 数据库：scf_loan
```

## 注意事项

1. **依赖要求**：此工具仅在Windows系统上测试过，需要MySQL已安装并添加到系统PATH环境变量中
2. **权限要求**：启动和停止MySQL服务可能需要管理员权限
3. **配置要求**：默认使用的MySQL配置为：
   - 主机：localhost
   - 端口：3306
   - 用户名：root
   - 密码：123456
   - 数据库：scf_loan
4. **安全注意事项**：此工具仅用于开发和测试环境，不建议在生产环境中使用

## 故障排除

### MySQL未安装

如果系统未安装MySQL，工具会提示您安装MySQL：

```
检查MySQL是否安装...
MySQL未安装，请先安装MySQL！
下载地址：https://dev.mysql.com/downloads/mysql/
```

### 无法启动MySQL服务

如果无法启动MySQL服务，可能是因为：

1. MySQL未正确安装
2. 缺少管理员权限
3. MySQL配置错误

请检查MySQL安装和配置，确保正确安装并具有足够的权限。

### 无法连接到MySQL服务

如果无法连接到MySQL服务，可能是因为：

1. MySQL服务未启动
2. MySQL连接配置错误
3. 防火墙阻止了连接

请检查MySQL服务状态和连接配置，确保服务已启动且配置正确。

## 支持的平台

- Windows
- macOS
- Linux

## 版本历史

### 1.0.0 (2026-02-04)

- 初始版本
- 支持启动、停止、检查状态和查看连接信息
- 支持Windows、macOS和Linux平台