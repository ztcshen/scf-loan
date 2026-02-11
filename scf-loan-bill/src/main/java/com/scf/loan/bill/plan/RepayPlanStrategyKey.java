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

    public RepayMethod getRepayMethod() {
        return repayMethod;
    }

    public void setRepayMethod(RepayMethod repayMethod) {
        this.repayMethod = repayMethod;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public String getDayCountConvention() {
        return dayCountConvention;
    }

    public void setDayCountConvention(String dayCountConvention) {
        this.dayCountConvention = dayCountConvention;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getGraceType() {
        return graceType;
    }

    public void setGraceType(String graceType) {
        this.graceType = graceType;
    }

    public String getSettlementMode() {
        return settlementMode;
    }

    public void setSettlementMode(String settlementMode) {
        this.settlementMode = settlementMode;
    }
}
