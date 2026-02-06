---
name: git-commit
description: Use this skill to automate Git commits with standardized messages, running tests and builds before committing. Trigger when the user wants to "commit code", "save changes", or "push updates".
---

# Git Commit Automation

This skill enforces quality checks (Maven build, unit tests) and standardized commit messages before committing code to Git.

## Usage

### 1. Basic Commit

Commits code with a message, automatically running tests and build first.

```bash
python git_commit.py -m "feat: Add financing order service"
```

### 2. Skip Checks (Emergency)

Use for documentation updates or emergency fixes where build/test is not needed.

```bash
python git_commit.py -m "docs: Update README" --skip-build --skip-tests
```

### 3. Specify Branch

Commit to a specific branch (if not current).

```bash
python git_commit.py -m "fix: Bug in calculation" --branch feature/calc-fix
```

## Commit Message Convention

Follows the Conventional Commits specification:
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation only
- `style:` Formatting, missing semi colons, etc; no code change
- `refactor:` Refactoring production code
- `test:` Adding tests, refactoring test; no production code change
- `chore:` Updating build tasks, package manager configs, etc; no production code change
      command: "工具会自动执行测试、编译，成功后完成 Commit"
  limitations:
    - "仅支持 Maven 项目结构的自动构建与测试"
    - "需要 Git 命令行工具支持"
    - "提交信息需符合团队规范（建议）"
  troubleshooting:
    - problem: "Git 仓库未初始化"
      solution: "在项目根目录执行 git init"
    - problem: "Maven 命令未找到"
      solution: "检查 Maven 环境变量配置"
    - problem: "单元测试失败"
      solution: "根据日志修复测试错误，或使用 --skip-tests 强制提交（不推荐）"
    - problem: "编译失败"
      solution: "修复代码编译错误"
  changelog:
    - version: "1.0.0"
      date: "2026-02-06"
      changes:
        - "初始版本，支持自动化提交工作流"
        - "集成单元测试与 Maven 编译检查"
        - "支持改动点自动总结"
