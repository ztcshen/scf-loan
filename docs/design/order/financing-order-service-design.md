# 技术方案设计：信贷核心-融资订单服务 (Financing Order Service)

| 属性 | 内容 |
| :--- | :--- |
| **作者** | scf-loan-team |
| **状态** | Draft |
| **最后更新** | 2026-02-06 |
| **受众** | 后端开发, 测试人员 |

## 1. 背景与目标 (Context & Goals)
### 1.1 背景
当前信贷系统处于从 0 到 1 的搭建阶段。作为核心业务模块，“融资订单服务”承载着从用户发起借款到最终放款的全流程状态流转。需要支持高并发下的订单创建、状态机的准确流转以及与下游风控、资金方的解耦交互。

### 1.2 目标
- 实现融资订单的全生命周期管理（申请 -> 审批 -> 放款 -> 结清/逾期）。
- 设计可扩展的状态机模式，支持未来新增业务状态。
- 确保资金操作（放款、还款）的数据一致性（幂等性）。
- 提供标准化的 API 供前端和其他微服务调用。

## 2. 核心概念 (Key Concepts)
- **融资申请 (Financing Apply)**: 用户发起的借款请求。
- **授信额度 (Credit Limit)**: 用户的可用借款额度，放款时需扣减，结清后恢复。
- **订单状态 (Order Status)**: 
  - `APPLYING`: 审批中
  - `ACCEPT`: 审批通过（待放款）
  - `REFUSED`: 审批拒绝
  - `LOANING`: 放款中
  - `REPAYING`: 还款中（放款成功）
  - `CLEAR_UP`: 已结清
  - `OVER_DUE`: 逾期

## 3. 详细设计 (Detailed Design)

### 3.1 领域模型 (Domain Model)
主要实体为 `FinancingOrder`。

```java
public class FinancingOrderEntity {
    private Long id;
    private String financingOrderId; // 业务主键
    private String uid;              // 用户ID
    private BigDecimal loanAmount;   // 放款金额
    private String applyStatus;      // 审批状态
    private String loanConfirmStatus;// 放款状态
    private String repayStatus;      // 还款状态
    private Date startDate;          // 起息日
    private Date dueDate;            // 到期日
    // ... 审计字段 (created_at, updated_by)
}
```

### 3.2 接口设计 (API Design)

#### 3.2.1 创建融资申请
- **URL**: `POST /api/financing/apply`
- **Request**:
  ```json
  {
    "uid": "USER123",
    "amount": 10000.00,
    "term": 30,
    "contractNo": "HT20260206001"
  }
  ```
- **Response**: `{"orderId": "FO202602060001"}`

#### 3.2.2 状态流转 (Status Transition)
采用事件驱动 + 状态机模式。
- **Event: 审批通过** -> `applyStatus` 变更为 `ACCEPT`
- **Event: 资金方放款成功** -> `loanConfirmStatus` 变更为 `LOAN_SUCCESS` -> 触发生成还款计划

### 3.3 数据库设计 (Schema)
对应表 `t_scf_financing_order`，已通过 `db-generate` skill 生成基础代码。
关键索引：
- `uk_financing_order_id` (Unique)
- `idx_uid_status` (uid, apply_status) - 查询用户当前订单

## 4. 关键流程 (Key Flows)

### 4.1 放款流程
1. 用户提交申请 -> 校验额度 -> 落库(APPLYING)
2. 异步调用风控系统 -> 风控回调 -> 更新状态(ACCEPT/REFUSED)
3. 若 ACCEPT -> 发起放款指令 -> 资金方处理
4. 资金方回调 -> 更新状态(LOAN_SUCCESS) -> **开启事务**:
   - 更新订单状态为 REPAYING
   - 生成还款计划 (RepaymentPlan)
   - 扣减最终额度

## 5. 风险与对策 (Risks & Mitigation)
- **并发重复提交**: 在 Controller 层增加分布式锁（基于 Redis，Key=`uid:apply`）。
- **状态不一致**: 核心状态流转使用数据库乐观锁 (`revision` 字段)。
- **外部调用超时**: 所有外部调用（风控、资方）均通过 MQ 异步解耦，并设置重试机制。

## 6. 测试计划 (Test Plan)
- **单元测试**: 覆盖 `FinancingOrderService` 的所有状态流转逻辑。
- **集成测试**: 模拟风控和资方的回调接口，验证全流程闭环。
- **并发测试**: 使用 JMeter 模拟 100 QPS 下的并发申请，验证锁机制。
