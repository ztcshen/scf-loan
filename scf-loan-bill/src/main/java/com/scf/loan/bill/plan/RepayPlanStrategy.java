package com.scf.loan.bill.plan;

import com.scf.loan.common.dto.RepayPlanItem;

import java.util.List;

public interface RepayPlanStrategy {
    RepayPlanStrategyKey key();

    /**
     * 生成还款计划
     * <p>
     * 计息规则说明（算头不算尾）：
     * 1. 起息日：{@link RepayPlanRequest#getLoanDate()}，计入计息天数。
     * 2. 到期日：不计入计息天数（即资金占用至到期日的前一天）。
     * 3. 计息天数 = 到期日 - 起息日。
     * <p>
     * 特殊场景示例（假设放款日为 T）：
     * - T日放款，T+1日还款：占用 1 天，计息 1 天。
     * - T日放款，T日还款（当日结清）：占用 < 1 天，按 1 天计息。
     *
     * @param request 还款计划生成请求参数
     * @return 还款计划明细列表
     */
    List<RepayPlanItem> generate(RepayPlanRequest request);
}
