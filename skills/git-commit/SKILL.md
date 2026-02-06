---
name: git-commit
description: 自动化 Git 代码提交工具，集成代码检查、单元测试、编译验证和改动总结功能，确保提交代码的质量和规范性。
metadata:
  emoji: "📦"
  requires:
    - name: python
      type: binary
      description: Python 运行环境
    - name: git
      type: binary
      description: Git 版本控制工具
    - name: maven
      type: binary
      description: Maven 构建工具
  install:
    - name: python
      command: "安装 Python 3.6+: https://www.python.org/downloads/"
    - name: git
      command: "安装 Git: https://git-scm.com/downloads"
    - name: maven
      command: "安装 Maven 3.6+: https://maven.apache.org/download.cgi"
    - name: dependencies
      command: "pip install -r requirements.txt (PyYAML, Click)"
  examples:
    - name: 基本提交
      command: "python git_commit.py --message \"feat: 添加用户登录功能\""
    - name: 跳过测试提交（紧急修复）
      command: "python git_commit.py --message \"fix: 修复NPE问题\" --skip-tests"
    - name: 跳过编译提交
      command: "python git_commit.py --message \"docs: 更新文档\" --skip-build"
    - name: 指定分支提交
      command: "python git_commit.py --message \"chore: 合并分支\" --branch develop"
    - name: 详细模式
      command: "python git_commit.py --message \"refactor: 重构核心逻辑\" --verbose"
  tags:
    - git
    - automation
    - workflow
    - testing
    - maven
  author: scf-team
  version: "1.0.0"
  category: devops
  platforms:
    - windows
    - macos
    - linux
  support:
    issues: "https://github.com/scf-team/scf-loan/issues"
    email: "support@scf-team.com"
  license:
    name: MIT
    url: "https://opensource.org/licenses/MIT"
  privacy:
    policy: "本工具仅在本地运行，不收集任何数据"
    compliance: "符合 GDPR 和其他数据保护法规"
  security:
    vulnerabilities: "无已知漏洞"
    best_practices: "不要在提交信息中包含敏感凭据"
  dependencies:
    - name: python
      version: ">=3.6"
    - name: git
      version: ">=2.0"
    - name: maven
      version: ">=3.6"
    - name: PyYAML
      version: "*"
    - name: Click
      version: "*"
  configuration:
    - name: message
      type: string
      description: 提交信息（必填）
      required: true
      alias: "-m"
    - name: branch
      type: string
      description: 目标分支（默认当前分支）
      required: false
      alias: "-b"
    - name: skip-tests
      type: boolean
      description: 跳过单元测试
      default: false
    - name: skip-build
      type: boolean
      description: 跳过编译验证
      default: false
    - name: verbose
      type: boolean
      description: 输出详细日志
      default: false
  usage:
    - step: "准备工作"
      command: "确保项目已初始化 Git 仓库，且 Maven 环境可用"
    - step: "执行提交"
      command: "python git_commit.py -m \"提交说明\""
    - step: "查看结果"
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
