# SCF Loan Risk

供应链金融风控项目，提供基础的规则评分能力和对外 REST API。

## 功能
- 评估融资申请风险等级（LOW / MEDIUM / HIGH / REJECT）
- 输出风险分、审批建议和触发规则原因
- 支持企业基础风控参数校验

## API
- `POST /api/risk/evaluate`

请求示例：
```json
{
  "enterpriseName": "杭州某供应链公司",
  "loanAmount": 600000,
  "invoiceAmount": 1000000,
  "overdueDays": 5,
  "historicalDefaultCount": 0,
  "coreEnterpriseConfirmed": true
}
```

