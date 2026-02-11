## t_route_capital_config 字段清单

来源：[核心系统表结构.md](file:///d:/IdeaProjects/scf-loan/docs/design/%E6%A0%B8%E5%BF%83%E7%B3%BB%E7%BB%9F%E8%A1%A8%E7%BB%93%E6%9E%84.md#L2385-L2414)

```
id
capital_project_code
capital_name
product_code
sub_product_code
enable_flag
stride_product
passed_valid_days
rejected_valid_days
required_inq_credit
required_sign_nucc
required_upload_document
max_upload_timeout_time
upload_sync_flag
loan_freq
pre_approval_dimension
maximum_amount
capital_abbr
pre_approval_start_time
pre_approval_end_time
sort_no
status
rate_range
created_time
updated_time
```

## 字段说明

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| id | bigint auto_increment | 自增主键 |
| capital_project_code | varchar(30) |  |
| capital_name | varchar(20) | 资金模式名称 |
| product_code | varchar(20) | 产品code |
| sub_product_code | varchar(20) | 子产品code |
| enable_flag | tinyint unsigned | 有效标识(0 无效 1有效) |
| stride_product | tinyint | 审批结果是否可跨产品 |
| passed_valid_days | int | 审批通过有效期 |
| rejected_valid_days | int | 审批拒绝时效 |
| required_inq_credit | tinyint | 是否需要查征 |
| required_sign_nucc | tinyint | 是否需签署网签协议 |
| required_upload_document | tinyint | 是否需要上传影像文件 |
| max_upload_timeout_time | tinyint | 最大文件上传超时时间，单位 秒 |
| upload_sync_flag | tinyint | 0--文件由单独接口上传 1--文件作为预审批接口的参数上传 |
| loan_freq | varchar(10) | 资方还款间隔 |
| pre_approval_dimension | varchar(10) | 预审批维度：0--订单维度 1--客户维度 |
| maximum_amount | bigint | 资方每日最大限额金额（万元） |
| capital_abbr | varchar(100) | 资金简称 |
| pre_approval_start_time | varchar(20) | 资方预审批开始时间（筛选时间） |
| pre_approval_end_time | varchar(20) | 资方预审批结束时间（筛选时间） |
| sort_no | int | 排序号码 |
| status | int | 资方配置状态 1:上线 2：下线 |
| rate_range | varchar(255) | 排除费率 |
| created_time | datetime | 创建时间 |
| updated_time | datetime | 更新时间 |
