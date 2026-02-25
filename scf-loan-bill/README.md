# SCF Loan Bill Module

## 简介
`scf-loan-bill` 是供应链金融系统的核心账单与还款模块，负责处理贷款还款计划的生成、还款试算以及相关的账单管理功能。

## 核心功能

### 1. 还款计划生成 (Repayment Plan Generation)
提供基于不同还款方式的计划生成服务。
- **服务接口**: `RepayPlanService`
- **主要方法**: `generatePlan(RepayPlanRequest request)`
- **支持策略**:
  - 等额本金 (`EQUAL_PRINCIPAL`)
  - 等额本息 (`EQUAL_PRINCIPAL_INTEREST`)
  - 先息后本 (`INTEREST_FIRST`)

### 2. 还款试算 (Repayment Trial)
在实际生成还款计划前，提供试算功能以预览还款详情。
- **服务接口**: `RepayTrialService`
- **主要方法**: `trial(RepayTrialRequest request)`
- **特点**:
  - 支持动态计算剩余本金
  - 复用还款计划生成逻辑，确保试算与实际计划一致

## 核心模型

### 请求模型
- **RepayPlanRequest**: 包含本金、年利率、期数、还款方式、起息日等关键参数。
- **RepayTrialRequest**: 试算请求，包含计划生成参数及可能的期次调整详情。

### 结果模型
- **RepayPlanItem**: 单期还款计划详情（期号、起止日期、应还金额明细）。
- **RepayTrialResult**: 试算结果汇总，包含总利息及各期详情。
- **RepayTrialScheduleItem**: 试算计划明细项（已精简冗余字段，仅保留核心日期与明细）。

## 目录结构
- `plan/`: 核心领域模型与枚举
  - `enums/`: 还款方式、计息方式等枚举
  - `strategy/`: 还款策略实现
- `service/`: 服务接口定义
- `service/impl/`: 业务逻辑实现

## 依赖说明
本模块依赖 `scf-loan-common` 获取公共DTO与工具类。
