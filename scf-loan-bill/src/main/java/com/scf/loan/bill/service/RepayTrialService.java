package com.scf.loan.bill.service;

import com.scf.loan.bill.plan.RepayTrialRequest;
import com.scf.loan.bill.plan.RepayTrialResult;

public interface RepayTrialService {
    /**
     * 执行还款试算
     * <p>
     * 计息逻辑说明：
     * 遵循“算头不算尾”原则，即：
     * 1. 试算金额基于生成的还款计划（RepayPlanService）。
     * 2. 资金占用天数 = 试算日（或到期日） - 起息日。
     * 3. 示例：T日放款，T+1日试算（或还款），计息天数为 1 天。
     *
     * @param request 试算请求参数
     * @return 试算结果
     */
    RepayTrialResult trial(RepayTrialRequest request);
}
