package com.scf.loan.bill.service.impl;

import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.bill.plan.RepayPlanStrategyRouter;
import com.scf.loan.bill.plan.strategy.EqualPrincipalInterestStrategy;
import com.scf.loan.bill.plan.strategy.EqualPrincipalStrategy;
import com.scf.loan.bill.plan.strategy.InterestFirstPrincipalLastStrategy;
import com.scf.loan.bill.plan.enums.RepayMethod;
import com.scf.loan.common.dto.RepayPlanItem;
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
        assertEquals(50L, first.getPrincipal());
        assertEquals(100L, first.getInterest());
        assertEquals(150L, first.getTotal());
        assertEquals(50L, first.getRemainingPrincipal());
    }

    @Test
    public void testGenerateEqualPrincipalInterestPlan() {
        RepayPlanServiceImpl service = buildService();
        RepayPlanRequest request = buildRequest(RepayMethod.EQUAL_PRINCIPAL_INTEREST);

        List<RepayPlanItem> items = service.generatePlan(request);

        assertEquals(2, items.size());

        RepayPlanItem first = items.get(0);
        assertEquals(133L, first.getTotal());
        assertEquals(33L, first.getPrincipal());
        assertEquals(100L, first.getInterest());

        RepayPlanItem second = items.get(1);
        assertEquals(134L, second.getTotal());
        assertEquals(67L, second.getPrincipal());
        assertEquals(67L, second.getInterest());
        assertEquals(0L, second.getRemainingPrincipal());
    }

    @Test
    public void testGenerateInterestFirstPlan() {
        RepayPlanServiceImpl service = buildService();
        RepayPlanRequest request = buildRequest(RepayMethod.INTEREST_FIRST);

        List<RepayPlanItem> items = service.generatePlan(request);

        assertEquals(2, items.size());

        RepayPlanItem first = items.get(0);
        assertEquals(0L, first.getPrincipal());
        assertEquals(100L, first.getInterest());
        assertEquals(100L, first.getTotal());
        assertEquals(100L, first.getRemainingPrincipal());

        RepayPlanItem second = items.get(1);
        assertEquals(100L, second.getPrincipal());
        assertEquals(100L, second.getInterest());
        assertEquals(200L, second.getTotal());
        assertEquals(0L, second.getRemainingPrincipal());
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
}
