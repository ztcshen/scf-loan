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
    private RepayMethod repayMethod;
    private String interestType;
    private String periodUnit;
    private String dayCountConvention;
    private String rateType;
    private String graceType;
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
