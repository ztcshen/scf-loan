---
name: db-generate
description: 数据库表代码生成工具，根据数据库表结构自动生成实体类、Mapper、Service、Controller 等基础代码，并提供代码验证和单元测试生成功能
metadata:
  emoji: "🗄️"
  requires:
    - name: mysql
      type: binary
      description: MySQL 客户端工具
    - name: java
      type: binary
      description: Java 开发环境
    - name: maven
      type: binary
      description: Maven 构建工具
  install:
    - name: mysql
      command: "下载并安装 MySQL 客户端: https://dev.mysql.com/downloads/mysql/"
    - name: java
      command: "下载并安装 JDK 8+: https://www.oracle.com/java/technologies/downloads/"
    - name: maven
      command: "下载并安装 Maven: https://maven.apache.org/download.cgi"
  examples:
    - name: 生成单个表的代码
      command: "python db_generate.py --table t_scf_financing_order --output-dir scf-loan-dal/src/main/java"
    - name: 生成多个表的代码
      command: "python db_generate.py --tables t_scf_financing_order,t_scf_repayment_plan --output-dir scf-loan-dal/src/main/java"
    - name: 生成完整模块代码
      command: "python db_generate.py --table t_scf_financing_order --output-dir . --full-module"
    - name: 生成代码并验证
      command: "python db_generate.py --table t_scf_financing_order --validate"
    - name: 生成单元测试
      command: "python db_generate.py --table t_scf_financing_order --generate-tests"
  tags:
    - database
    - code-generation
    - java
    - mybatis-plus
    - validation
    - unit-test
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
    - name: pymysql
      version: "^1.0.2"
    - name: jinja2
      version: "^3.0.3"
    - name: click
      version: "^8.0.4"
    - name: pycodestyle
      version: "^2.8.0"
  configuration:
    - name: db_url
      type: string
      description: 数据库连接 URL
      required: true
    - name: db_username
      type: string
      description: 数据库用户名
      required: true
    - name: db_password
      type: string
      description: 数据库密码
      required: true
    - name: base_package
      type: string
      description: 基础包名
      default: "com.scf.loan"
    - name: output_dir
      type: string
      description: 输出目录
      default: "."
    - name: validate_code
      type: boolean
      description: 是否验证生成的代码
      default: false
    - name: generate_tests
      type: boolean
      description: 是否生成单元测试
      default: false
  usage:
    - step: "配置数据库连接信息"
      command: "在 config.yml 中设置数据库连接参数"
    - step: "运行代码生成工具"
      command: "python db_generate.py --table <table_name>"
    - step: "验证生成的代码"
      command: "python db_generate.py --table <table_name> --validate"
    - step: "生成单元测试"
      command: "python db_generate.py --table <table_name> --generate-tests"
    - step: "查看生成的代码"
      command: "检查输出目录中的生成文件"
    - step: "集成到项目中"
      command: "将生成的代码复制到对应模块"
  limitations:
    - "仅支持 MySQL 数据库"
    - "仅支持 MyBatis Plus 框架"
    - "复杂的表关系可能需要手动调整"
  troubleshooting:
    - problem: "数据库连接失败"
      solution: "检查数据库连接参数和网络连接"
    - problem: "生成的代码编译错误"
      solution: "检查 Java 环境和依赖配置"
    - problem: "表结构不存在"
      solution: "确保指定的表在数据库中存在"
    - problem: "代码验证失败"
      solution: "根据验证错误信息修改代码"
  changelog:
    - version: "1.0.0"
      date: "2026-02-04"
      changes:
        - "初始版本，支持基本的代码生成功能"
        - "支持 MySQL 数据库表结构解析"
        - "生成实体类、Mapper、Service、Controller 代码"
        - "添加代码验证功能"
        - "添加单元测试生成功能"
