package com.scf.loan.bill.plan;

import com.scf.loan.bill.plan.enums.DayCountConvention;
import com.scf.loan.bill.plan.enums.GraceType;
import com.scf.loan.bill.plan.enums.InterestType;
import com.scf.loan.bill.plan.enums.PeriodUnit;
import com.scf.loan.bill.plan.enums.RateType;
import com.scf.loan.bill.plan.enums.RepayMethod;
import com.scf.loan.bill.plan.enums.SettlementMode;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepayPlanRequestTest {

    @Test
    public void testRepayPlanRequestDataMethods() {
        RepayPlanRequest request1 = buildRequest();
        request1.setInterestType(InterestType.DAILY.getCode());
        request1.setPeriodUnit(PeriodUnit.DAY.getCode());
        request1.setDayCountConvention(DayCountConvention.ACT_365.getCode());
        request1.setRateType(RateType.FIXED.getCode());
        request1.setGraceType(GraceType.NONE.getCode());
        request1.setSettlementMode(SettlementMode.ON_DUE.getCode());

        RepayPlanRequest request2 = buildRequest();
        request2.setInterestType(InterestType.DAILY.getCode());
        request2.setPeriodUnit(PeriodUnit.DAY.getCode());
        request2.setDayCountConvention(DayCountConvention.ACT_365.getCode());
        request2.setRateType(RateType.FIXED.getCode());
        request2.setGraceType(GraceType.NONE.getCode());
        request2.setSettlementMode(SettlementMode.ON_DUE.getCode());

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertEquals(RepayMethod.EQUAL_PRINCIPAL, request1.getRepayMethod());
        assertEquals(InterestType.DAILY.getCode(), request1.getInterestType());
        assertEquals(PeriodUnit.DAY.getCode(), request1.getPeriodUnit());
        assertEquals(DayCountConvention.ACT_365.getCode(), request1.getDayCountConvention());
        assertEquals(RateType.FIXED.getCode(), request1.getRateType());
        assertEquals(GraceType.NONE.getCode(), request1.getGraceType());
        assertEquals(SettlementMode.ON_DUE.getCode(), request1.getSettlementMode());
        assertEquals(2_0000_0000L, request1.getPenaltyDailyRate());
        assertEquals(false, request1.equals(null));
        assertEquals(false, request1.equals(new Object()));
        assertEquals(true, request1.toString().contains("RepayPlanRequest"));
        request2.setRateType(RateType.FLOAT.getCode());
        assertEquals(false, request1.equals(request2));
    }

    private RepayPlanRequest buildRequest() {
        RepayPlanRequest request = new RepayPlanRequest();
        request.setRepayMethod(RepayMethod.EQUAL_PRINCIPAL);
        request.setPrincipal(100L);
        request.setDailyRate(1_0000_0000L);
        request.setPenaltyDailyRate(2_0000_0000L);
        request.setLoanDate(LocalDate.of(2026, 2, 1));
        request.setPeriodDays(1);
        request.setPeriodCount(2);
        return request;
    }
}
