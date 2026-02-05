# Skills 目录说明

本目录包含了多个用于辅助开发和管理 scf-loan 项目的工具（Skill）。这些工具可以帮助开发者更高效地完成各种开发任务，如代码生成、数据库操作、Git 提交和服务运行等。

## 目录结构

```
skills/
├── db-generate/         # 数据库表代码生成工具（不生成Controller）
├── db-remove/           # 数据库表代码清理工具
├── git-commit/          # Git 提交工具
├── service-runner/      # 服务编译运行工具
└── README.md            # 本说明文件
```

## 工具说明

### 1. db-generate（数据库表代码生成工具）

**功能**：根据数据库表结构生成对应的 Java 代码，包括实体类、Mapper 接口、DTO 类、Service 接口、Service 实现类和单元测试类（当前版本不生成 Controller）。

**使用方法**：

```bash
python skills/db-generate/db_generate.py --table <table_name> [--generate-tests]
```

**参数说明**：
- `--table`：指定要生成代码的数据库表名
- `--generate-tests`：是否生成单元测试文件

**详细信息**：请查看 `db-generate/SKILL.md` 文件

### 2. db-remove（数据库表代码清理工具）

**功能**：删除之前通过 db-generate 工具生成的代码文件。

**使用方法**：

```bash
python skills/db-remove/db_remove.py --table <table_name>
```

**参数说明**：
- `--table`：指定要清理代码的数据库表名

**详细信息**：请查看 `db-remove/SKILL.md` 文件

### 3. git-commit（Git 提交工具）

**功能**：自动执行 Git 提交操作，包括添加所有修改的文件、提交并推送。

**使用方法**：

```bash
python skills/git-commit/git_commit.py --message <commit_message>
```

**参数说明**：
- `--message`：指定 Git 提交信息

**详细信息**：请查看 `git-commit/SKILL.md` 文件

### 4. service-runner（服务编译运行工具）

**功能**：编译和运行 scf-loan-web 服务，并可选检测服务是否能正常启动运行（默认跳过健康检查）。

**使用方法**：

```bash
python skills/service-runner/service_runner.py [--project-dir <project_dir>] [--skip-health]
```

**参数说明**：
- `--project-dir`：指定项目根目录，默认为当前目录

**详细信息**：请查看 `service-runner/SKILL.md` 文件

## 依赖要求

- Python 3.6+
- 各工具的具体依赖请查看对应目录下的 SKILL.md 文件

## 注意事项

1. **数据库连接**：db-generate 支持数据库连接；连接失败时自动降级为默认DDL解析，无需手动干预。

2. **Maven 环境**：service-runner 工具需要 Maven 环境，请确保 Maven 已安装并添加到系统 PATH 环境变量中。

3. **Git 环境**：git-commit 工具需要 Git 环境，请确保 Git 已安装并配置了正确的用户信息。

4. **权限问题**：请确保对项目目录有读写权限，以便工具能够正常创建和修改文件。

## 扩展开发

如果需要开发新的工具，可以按照以下步骤进行：

1. 在 `skills` 目录下创建一个新的子目录，目录名即为工具名称

2. 在新目录中创建必要的 Python 脚本和配置文件

3. 创建 `SKILL.md` 文件，详细说明工具的功能、使用方法和依赖要求

4. 确保工具的入口脚本使用 `click` 库进行命令行参数解析，以便与其他工具保持一致的使用方式

## 联系信息

如有任何问题或建议，请联系项目维护人员。
