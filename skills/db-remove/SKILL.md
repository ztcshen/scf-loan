---
name: db-remove
description: 数据库表代码清理工具，根据数据库表名自动删除对应的实体类、Mapper、Service、Controller 等相关代码文件
metadata:
  emoji: "🗑️"
  requires:
    - name: java
      type: binary
      description: Java 开发环境
    - name: maven
      type: binary
      description: Maven 构建工具
  install:
    - name: java
      command: "下载并安装 JDK 8+: https://www.oracle.com/java/technologies/downloads/"
    - name: maven
      command: "下载并安装 Maven: https://maven.apache.org/download.cgi"
  examples:
    - name: 删除单个表的代码
      command: "python db_remove.py --table t_scf_financing_order --base-dir scf-loan"
    - name: 删除多个表的代码
      command: "python db_remove.py --tables t_scf_financing_order,t_scf_repayment_plan --base-dir scf-loan"
    - name: 强制删除代码
      command: "python db_remove.py --table t_scf_financing_order --base-dir scf-loan --force"
    - name: 预览删除文件
      command: "python db_remove.py --table t_scf_financing_order --base-dir scf-loan --dry-run"
  tags:
    - database
    - code-cleanup
    - java
    - mybatis-plus
  author: scf-team
  version: "1.0.0"
  category: development
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
    best_practices: "使用参数化查询，避免 SQL 注入"
  dependencies:
    - name: click
      version: "^8.0.4"
  configuration:
    - name: base_dir
      type: string
      description: 项目基础目录
      required: true
    - name: base_package
      type: string
      description: 基础包名
      default: "com.scf.loan"
    - name: force
      type: boolean
      description: 是否强制删除
      default: false
    - name: dry_run
      type: boolean
      description: 是否仅预览删除文件，不实际删除
      default: false
  usage:
    - step: "确认要删除的表名"
      command: "确认数据库表已经废弃，准备删除相关代码"
    - step: "运行代码清理工具"
      command: "python db_remove.py --table <table_name> --base-dir <project_dir>"
    - step: "确认删除操作"
      command: "根据提示确认删除操作"
    - step: "查看删除结果"
      command: "检查删除的文件列表"
  limitations:
    - "仅删除根据表名生成的代码文件"
    - "不会删除数据库表结构本身"
    - "可能无法删除手动修改过的文件"
  troubleshooting:
    - problem: "找不到要删除的文件"
      solution: "确认表名和项目目录是否正确"
    - problem: "删除失败"
      solution: "检查文件权限，或使用 --force 参数"
    - problem: "误删文件"
      solution: "使用版本控制工具恢复文件"
  changelog:
    - version: "1.0.0"
      date: "2026-02-04"
      changes:
        - "初始版本，支持基本的代码删除功能"
        - "支持单个表和多个表的代码删除"
        - "支持预览删除文件（dry-run）"
        - "支持强制删除模式"
