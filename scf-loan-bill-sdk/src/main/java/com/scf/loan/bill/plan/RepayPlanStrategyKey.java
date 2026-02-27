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
}
