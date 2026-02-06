---
name: orchestrator
description: 编排工具，自动化执行从数据库表代码生成到服务运行的完整流程。
metadata:
  emoji: "🎼"
  requires:
    - name: python
      type: binary
      description: Python 运行环境
    - name: skill-db-generate
      type: skill
      description: 数据库代码生成技能
    - name: skill-service-runner
      type: skill
      description: 服务运行技能
  install:
    - name: python
      command: "安装 Python 3.6+: https://www.python.org/downloads/"
    - name: dependencies
      command: "pip install click"
  examples:
    - name: 执行完整流程
      command: "python skills/orchestrator/orchestrator.py run"
    - name: 仅生成代码
      command: "python skills/orchestrator/orchestrator.py generate-code"
    - name: 仅运行服务
      command: "python skills/orchestrator/orchestrator.py run-service"
    - name: 清理资源
      command: "python skills/orchestrator/orchestrator.py cleanup"
  tags:
    - orchestration
    - workflow
    - automation
    - database
    - service
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
    best_practices: "避免在生产环境直接使用清理命令"
  dependencies:
    - name: python
      version: ">=3.6"
    - name: click
      version: "*"
  configuration:
    - name: command
      type: string
      description: 执行命令 (run, generate-code, run-service, cleanup)
      required: true
  usage:
    - step: "准备工作"
      command: "确保 Python 环境已安装，且相关 skill (db-generate, service-runner) 已配置"
    - step: "执行编排"
      command: "python skills/orchestrator/orchestrator.py run"
    - step: "查看输出"
      command: "检查控制台输出，确认各步骤执行状态"
  limitations:
    - "仅支持串行执行任务"
    - "依赖于特定的 skill 路径结构"
  troubleshooting:
    - problem: "ModuleNotFoundError"
      solution: "确保在项目根目录下运行命令"
    - problem: "Skill not found"
      solution: "检查 skills 目录下是否存在对应的 skill 文件夹"
  changelog:
    - version: "1.0.0"
      date: "2026-02-04"
      changes:
        - "初始版本，支持基本编排功能"
