# Skills 目录说明与上下文加载

本目录包含多个用于辅助开发和管理 scf-loan 项目的工具（Skill）。系统对技能的加载遵循“轻重分离”的上下文策略：优先加载技能的元信息以进行匹配，命中后再按需读取完整技能文件。

## 目录结构

```
skills/
├── db-generate/         # 代码生成技能（当前不生成Controller）
├── db-remove/           # 代码清理技能
├── git-commit/          # 提交流程技能
├── service-runner/      # 编译运行技能（健康检查可选）
└── README.md            # 本说明文件
```

## 上下文加载机制

- 元信息优先：系统会优先加载每个技能的 name 与 description 参与匹配
- 按需读取：只有在命中匹配时，才会读取并执行完整的技能脚本与 SKILL.md
- 版本与环境：支持通过环境变量（如 SCF_SKILL_VERSION）覆盖技能版本等元信息
- 副作用最小：技能应声明副作用与生成文件，便于安全执行与审计

## 元信息规范建议

- name：唯一、简洁、能表达动作或领域
- description：一句话精准描述目标与边界，包含关键检索词
- category/tags：便于主题与意图匹配（如 codegen、dto、pagination）
- version/changelog：记录能力演进，避免行为偏差
- generated-files/side-effects：列出可能写入/删除的文件与范围
- configuration：参数清单与默认值（环境变量优先）
- usage：典型调用示例与触发提示

## 目录结构

- 生成代码：在需要生成实体/Mapper/Service/DTO时，按需调用 db-generate
- 清理代码：在需要移除指定表相关代码时，按需调用 db-remove
- 提交流程：在需要标准化执行测试、编译并提交时，按需调用 git-commit
- 编译运行：在需要验证 web 模块运行时，按需调用 service-runner（健康检查默认跳过）

## 依赖要求与上下文

- Python 3.6+
- 各技能的具体依赖与配置请查看对应目录下的 SKILL.md 文件

## 注意事项与最佳实践

1. **数据库连接**：db-generate 支持数据库连接；连接失败时自动降级为默认DDL解析，无需手动干预

2. **Maven 环境**：service-runner 工具需要 Maven 环境，请确保 Maven 已安装并添加到系统 PATH 环境变量中。

3. **Git 环境**：git-commit 工具需要 Git 环境，请确保 Git 已安装并配置了正确的用户信息。

4. **权限问题**：请确保对项目目录有读写权限，以便工具能够正常创建和修改文件。

## 扩展开发与元信息

如果需要开发新的工具，可以按照以下步骤进行：

1. 在 `skills` 目录下创建一个新的子目录，目录名即为工具名称

2. 在新目录中创建必要的 Python 脚本和配置文件

3. 创建 `SKILL.md` 文件，聚焦元信息（name、description、tags、configuration、generated-files、side-effects、changelog）

4. 确保工具的入口脚本使用 `click` 库进行命令行参数解析，以便与其他工具保持一致的使用方式

## 联系信息

如有任何问题或建议，请联系项目维护人员。
