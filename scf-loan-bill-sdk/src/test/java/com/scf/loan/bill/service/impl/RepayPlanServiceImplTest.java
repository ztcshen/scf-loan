package com.scf.loan.bill.service.impl;

import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.bill.plan.RepayPlanStrategyRouter;
import com.scf.loan.bill.plan.strategy.EqualPrincipalInterestStrategy;
import com.scf.loan.bill.plan.strategy.EqualPrincipalStrategy;
import com.scf.loan.bill.plan.strategy.InterestFirstPrincipalLastStrategy;
import com.scf.loan.bill.plan.enums.RepayMethod;
import com.scf.loan.common.dto.RepayPlanItem;
import com.scf.loan.common.dto.RepayPlanSubjectDetail;
import com.scf.loan.common.enums.ChargeSubject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RepayPlanServiceImplTest {

    @Test
    public void testGenerateEqualPrincipalPlan() {
        RepayPlanServiceImpl service = buildService();
        RepayPlanRequest request = buildRequest(RepayMethod.EQUAL_PRINCIPAL);

        List<RepayPlanItem> items = service.generatePlan(request);

        assertEquals(2, items.size());

        RepayPlanItem first = items.get(0);
        assertEquals(1, first.getPeriod());
        assertEquals(LocalDate.of(2026, 2, 1), first.getStartDate());
        assertEquals(LocalDate.of(2026, 2, 2), first.getDueDate());
        assertEquals(50L, getAmount(first, ChargeSubject.PRINCIPAL));
        assertEquals(100L, getAmount(first, ChargeSubject.INTEREST));
    }

    @Test
    public void testGenerateEqualPrincipalInterestPlan() {
        RepayPlanServiceImpl service = buildService();
        RepayPlanRequest request = buildRequest(RepayMethod.EQUAL_PRINCIPAL_INTEREST);

        List<RepayPlanItem> items = service.generatePlan(request);

        assertEquals(2, items.size());

        RepayPlanItem first = items.get(0);
        assertEquals(33L, getAmount(first, ChargeSubject.PRINCIPAL));
        assertEquals(100L, getAmount(first, ChargeSubject.INTEREST));

        RepayPlanItem second = items.get(1);
        assertEquals(67L, getAmount(second, ChargeSubject.PRINCIPAL));
        assertEquals(67L, getAmount(second, ChargeSubject.INTEREST));
    }

    @Test
    public void testGenerateInterestFirstPlan() {
        RepayPlanServiceImpl service = buildService();
        RepayPlanRequest request = buildRequest(RepayMethod.INTEREST_FIRST);

        List<RepayPlanItem> items = service.generatePlan(request);

        assertEquals(2, items.size());

        RepayPlanItem first = items.get(0);
        assertEquals(0L, getAmount(first, ChargeSubject.PRINCIPAL));
        assertEquals(100L, getAmount(first, ChargeSubject.INTEREST));

        RepayPlanItem second = items.get(1);
        assertEquals(100L, getAmount(second, ChargeSubject.PRINCIPAL));
        assertEquals(100L, getAmount(second, ChargeSubject.INTEREST));
    }

    @Test
    public void testGeneratePlanWithInvalidParams() {
        RepayPlanServiceImpl service = buildService();
        RepayPlanRequest request = buildRequest(RepayMethod.EQUAL_PRINCIPAL);
        request.setPrincipal(0L);
        assertThrows(ConstraintViolationException.class, () -> service.generatePlan(request));
    }

    private RepayPlanServiceImpl buildService() {
        RepayPlanStrategyRouter router = new RepayPlanStrategyRouter(Arrays.asList(
                new EqualPrincipalStrategy(),
                new EqualPrincipalInterestStrategy(),
                new InterestFirstPrincipalLastStrategy()
        ));
        return new RepayPlanServiceImpl(router);
    }

    private RepayPlanRequest buildRequest(RepayMethod method) {
        RepayPlanRequest request = new RepayPlanRequest();
        request.setRepayMethod(method);
        request.setPrincipal(100L);
        request.setDailyRate(1_0000_0000L);
        request.setPenaltyDailyRate(2_0000_0000L);
        request.setLoanDate(LocalDate.of(2026, 2, 1));
        request.setPeriodDays(1);
        request.setPeriodCount(2);
        return request;
    }

    private long getAmount(RepayPlanItem item, ChargeSubject subject) {
        for (RepayPlanSubjectDetail detail : item.getAmountDetail()) {
            if (detail != null && subject == detail.getSubject()) {
                return detail.getAmount();
            }
        }
        return 0L;
    }
}
