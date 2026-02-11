package com.scf.loan.bill.service.impl;

import com.scf.loan.bill.plan.ChargeRate;
import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.bill.plan.RepayPlanStrategyRouter;
import com.scf.loan.bill.plan.RepayTrialRequest;
import com.scf.loan.bill.plan.RepayTrialResult;
import com.scf.loan.bill.plan.RepayTrialScheduleItem;
import com.scf.loan.bill.plan.RepayTrialSubjectAmount;
import com.scf.loan.bill.plan.enums.RateUnit;
import com.scf.loan.bill.plan.enums.RepayMethod;
import com.scf.loan.common.enums.ChargeSubject;
import com.scf.loan.bill.plan.strategy.EqualPrincipalInterestStrategy;
import com.scf.loan.bill.plan.strategy.EqualPrincipalStrategy;
import com.scf.loan.bill.plan.strategy.InterestFirstPrincipalLastStrategy;
import com.scf.loan.bill.service.RepayPlanService;
import com.scf.loan.bill.service.RepayTrialService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RepayTrialServiceImplTest {

    @Test
    public void testTrialWithChargeRates() {
        RepayTrialService service = buildService();
        RepayPlanRequest planRequest = buildRequest(RepayMethod.EQUAL_PRINCIPAL);
        ChargeRate guaranteeFee = new ChargeRate();
        guaranteeFee.setSubject(ChargeSubject.GUARANTEE_FEE);
        guaranteeFee.setRateUnit(RateUnit.DAILY);
        guaranteeFee.setRateValue(1_0000_0000L);
        planRequest.setChargeRates(Collections.singletonList(guaranteeFee));

        RepayTrialRequest request = new RepayTrialRequest();
        request.setPlanRequest(planRequest);
        request.setTrialDate(LocalDate.of(2026, 2, 1));

        RepayTrialResult result = service.trial(request);

        assertEquals(1, result.getPeriod());
        assertNotNull(result.getAmountDetail());
        assertEquals(4, result.getAmountDetail().size());
    }

    @Test
    public void testTrialWithRepaidAmounts() {
        RepayTrialService service = buildService();
        RepayPlanRequest planRequest = buildRequest(RepayMethod.EQUAL_PRINCIPAL);
        RepayTrialScheduleItem scheduleItem = new RepayTrialScheduleItem();
        scheduleItem.setPeriod(1);
        scheduleItem.setStartDate(LocalDate.of(2026, 2, 1));
        scheduleItem.setDueDate(LocalDate.of(2026, 2, 2));
        List<RepayTrialSubjectAmount> repaidDetails = new ArrayList<>();
        RepayTrialSubjectAmount repaidPrincipal = new RepayTrialSubjectAmount();
        repaidPrincipal.setSubject(ChargeSubject.PRINCIPAL);
        repaidPrincipal.setAmount(10L);
        repaidDetails.add(repaidPrincipal);
        RepayTrialSubjectAmount repaidInterest = new RepayTrialSubjectAmount();
        repaidInterest.setSubject(ChargeSubject.INTEREST);
        repaidInterest.setAmount(20L);
        repaidDetails.add(repaidInterest);
        RepayTrialSubjectAmount repaidPenalty = new RepayTrialSubjectAmount();
        repaidPenalty.setSubject(ChargeSubject.PENALTY);
        repaidPenalty.setAmount(0L);
        repaidDetails.add(repaidPenalty);
        scheduleItem.setRepaidDetails(repaidDetails);

        RepayTrialRequest request = new RepayTrialRequest();
        request.setPlanRequest(planRequest);
        request.setTrialDate(LocalDate.of(2026, 2, 1));
        request.setPeriodDetails(Collections.singletonList(scheduleItem));

        RepayTrialResult result = service.trial(request);

        assertNotNull(result.getAmountDetail());
        assertEquals(3, result.getAmountDetail().size());
    }

    private RepayTrialService buildService() {
        RepayPlanStrategyRouter router = new RepayPlanStrategyRouter(Arrays.asList(
                new EqualPrincipalStrategy(),
                new EqualPrincipalInterestStrategy(),
                new InterestFirstPrincipalLastStrategy()
        ));
        RepayPlanService repayPlanService = new RepayPlanServiceImpl(router);
        return new RepayTrialServiceImpl(repayPlanService);
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
