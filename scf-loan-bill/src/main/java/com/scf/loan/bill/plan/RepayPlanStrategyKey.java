package com.scf.loan.bill.plan;

import com.scf.loan.bill.plan.enums.RepayMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepayPlanStrategyKey {
    /**
     * 还款方式主键字段，用于确定基础策略。
     */
    private RepayMethod repayMethod;
    /**
     * 计息方式扩展字段，命中更细粒度策略时参与匹配。
     */
    private String interestType;
    /**
     * 期单位扩展字段，命中更细粒度策略时参与匹配。
     */
    private String periodUnit;
    /**
     * 计息天数约定扩展字段，命中更细粒度策略时参与匹配。
     */
    private String dayCountConvention;
    /**
     * 利率类型扩展字段，命中更细粒度策略时参与匹配。
     */
    private String rateType;
    /**
     * 宽限规则扩展字段，命中更细粒度策略时参与匹配。
     */
    private String graceType;
    /**
     * 结算方式扩展字段，命中更细粒度策略时参与匹配。
     */
    private String settlementMode;

    public RepayPlanStrategyKey fallback() {
        RepayPlanStrategyKey key = new RepayPlanStrategyKey();
        key.setRepayMethod(repayMethod);
        return key;
    }
}
