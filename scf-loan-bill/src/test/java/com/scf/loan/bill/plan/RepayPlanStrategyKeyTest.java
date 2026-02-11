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
        RepayPlanStrategyKey key = buildKey(RepayMethod.EQUAL_PRINCIPAL,
                InterestType.DAILY.getCode(),
                PeriodUnit.DAY.getCode(),
                null,
                null,
                null,
                null);
        RepayPlanStrategyKey fallback = key.fallback();
        assertEquals(RepayMethod.EQUAL_PRINCIPAL, fallback.getRepayMethod());
        assertEquals(null, fallback.getInterestType());
        assertEquals(null, fallback.getPeriodUnit());
    }

    @Test
    public void testRepayPlanStrategyKeyDataMethods() {
        RepayPlanStrategyKey key1 = buildKey(RepayMethod.EQUAL_PRINCIPAL,
                InterestType.DAILY.getCode(),
                PeriodUnit.DAY.getCode(),
                DayCountConvention.ACT_365.getCode(),
                RateType.FIXED.getCode(),
                GraceType.NONE.getCode(),
                SettlementMode.ON_DUE.getCode());
        RepayPlanStrategyKey key2 = buildKey(RepayMethod.EQUAL_PRINCIPAL,
                InterestType.DAILY.getCode(),
                PeriodUnit.DAY.getCode(),
                DayCountConvention.ACT_365.getCode(),
                RateType.FIXED.getCode(),
                GraceType.NONE.getCode(),
                SettlementMode.ON_DUE.getCode());
        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
        assertEquals(true, key1.toString().contains("RepayPlanStrategyKey"));
        assertEquals(false, key1.equals(null));
        assertEquals(false, key1.equals(new Object()));
        key2.setRateType(RateType.FLOAT.getCode());
        assertEquals(false, key1.equals(key2));
    }

    private RepayPlanStrategyKey buildKey(RepayMethod repayMethod,
                                          String interestType,
                                          String periodUnit,
                                          String dayCountConvention,
                                          String rateType,
                                          String graceType,
                                          String settlementMode) {
        RepayPlanStrategyKey key = new RepayPlanStrategyKey();
        key.setRepayMethod(repayMethod);
        key.setInterestType(interestType);
        key.setPeriodUnit(periodUnit);
        key.setDayCountConvention(dayCountConvention);
        key.setRateType(rateType);
        key.setGraceType(graceType);
        key.setSettlementMode(settlementMode);
        return key;
    }
}
