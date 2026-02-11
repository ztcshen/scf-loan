package com.scf.loan.bill.plan;

import com.scf.loan.bill.plan.enums.DayCountConvention;
import com.scf.loan.bill.plan.enums.GraceType;
import com.scf.loan.bill.plan.enums.InterestType;
import com.scf.loan.bill.plan.enums.PeriodUnit;
import com.scf.loan.bill.plan.enums.RateType;
import com.scf.loan.bill.plan.enums.RepayMethod;
import com.scf.loan.bill.plan.enums.SettlementMode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepayPlanStrategyKeyTest {

    @Test
    public void testStrategyKeyFallback() {
        RepayPlanStrategyKey key = RepayPlanStrategyKey.builder()
                .repayMethod(RepayMethod.EQUAL_PRINCIPAL)
                .interestType(InterestType.DAILY.getCode())
                .periodUnit(PeriodUnit.DAY.getCode())
                .build();
        RepayPlanStrategyKey fallback = key.fallback();
        assertEquals(RepayMethod.EQUAL_PRINCIPAL, fallback.getRepayMethod());
        assertEquals(null, fallback.getInterestType());
        assertEquals(null, fallback.getPeriodUnit());
    }

    @Test
    public void testRepayPlanStrategyKeyDataMethods() {
        RepayPlanStrategyKey key1 = RepayPlanStrategyKey.builder()
                .repayMethod(RepayMethod.EQUAL_PRINCIPAL)
                .interestType(InterestType.DAILY.getCode())
                .periodUnit(PeriodUnit.DAY.getCode())
                .dayCountConvention(DayCountConvention.ACT_365.getCode())
                .rateType(RateType.FIXED.getCode())
                .graceType(GraceType.NONE.getCode())
                .settlementMode(SettlementMode.ON_DUE.getCode())
                .build();
        RepayPlanStrategyKey key2 = RepayPlanStrategyKey.builder()
                .repayMethod(RepayMethod.EQUAL_PRINCIPAL)
                .interestType(InterestType.DAILY.getCode())
                .periodUnit(PeriodUnit.DAY.getCode())
                .dayCountConvention(DayCountConvention.ACT_365.getCode())
                .rateType(RateType.FIXED.getCode())
                .graceType(GraceType.NONE.getCode())
                .settlementMode(SettlementMode.ON_DUE.getCode())
                .build();
        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
        assertEquals(true, key1.toString().contains("RepayPlanStrategyKey"));
        assertEquals(false, key1.equals(null));
        assertEquals(false, key1.equals(new Object()));
        key2.setRateType(RateType.FLOAT.getCode());
        assertEquals(false, key1.equals(key2));
    }
}
