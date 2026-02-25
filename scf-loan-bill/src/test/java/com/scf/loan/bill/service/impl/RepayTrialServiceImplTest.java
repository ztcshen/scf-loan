package com.scf.loan.bill.service.impl;

import com.scf.loan.bill.plan.ChargeRate;
import com.scf.loan.bill.plan.RepayPlanStrategyRouter;
import com.scf.loan.bill.plan.RepayTrialRequest;
import com.scf.loan.bill.plan.RepayTrialResult;
import com.scf.loan.bill.plan.RepayTrialScheduleItem;
import com.scf.loan.bill.plan.RepayTrialSubjectAmount;
import com.scf.loan.bill.plan.RepayTrialSubjectDetail;
import com.scf.loan.bill.plan.enums.RateUnit;
import com.scf.loan.bill.plan.enums.RepayMethod;
import com.scf.loan.common.enums.ChargeSubject;
import com.scf.loan.bill.plan.strategy.EqualPrincipalInterestStrategy;
import com.scf.loan.bill.plan.strategy.EqualPrincipalStrategy;
import com.scf.loan.bill.plan.strategy.InterestFirstPrincipalLastStrategy;
import com.scf.loan.bill.service.RepayPlanService;
import com.scf.loan.bill.service.RepayTrialService;
import com.scf.loan.common.dto.RepayPlanItem;
import com.scf.loan.common.dto.RepayPlanSubjectDetail;
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
        RepayTrialRequest request = buildRequest(RepayMethod.EQUAL_PRINCIPAL);
        ChargeRate guaranteeFee = new ChargeRate();
        guaranteeFee.setSubject(ChargeSubject.GUARANTEE_FEE);
        guaranteeFee.setRateUnit(RateUnit.DAILY);
        guaranteeFee.setRateValue(1_0000_0000L);
        request.setChargeRates(Collections.singletonList(guaranteeFee));
        request.setTrialDate(LocalDate.of(2026, 2, 1));

        RepayTrialResult result = service.trial(request);

        assertEquals(1, result.getPeriod());
        assertNotNull(result.getAmountDetail());
        assertEquals(4, result.getAmountDetail().size());
    }

    @Test
    public void testTrialWithRepaidAmounts() {
        RepayTrialService service = buildService();
        RepayTrialRequest request = buildRequest(RepayMethod.EQUAL_PRINCIPAL);
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

        request.setTrialDate(LocalDate.of(2026, 2, 1));
        request.setPeriodDetails(Collections.singletonList(scheduleItem));

        RepayTrialResult result = service.trial(request);

        assertNotNull(result.getAmountDetail());
        assertEquals(3, result.getAmountDetail().size());
    }

    @Test
    public void testTrialInterestDaysLogic() {
        RepayTrialService service = buildService();
        RepayTrialRequest request = buildRequest(RepayMethod.EQUAL_PRINCIPAL);
        
        // 设置测试参数
        long principal = 1000000L; // 100万分 = 1万
        request.setPrincipal(principal);
        
        // 设置日利率为 0.05% (万分之五)
        // 1 = 10^-8
        // 0.0005 = 50000 * 10^-8
        long dailyRate = 50000L;
        request.setDailyRate(dailyRate);
        
        // 设置放款日为 2026-02-01
        LocalDate loanDate = LocalDate.of(2026, 2, 1);
        request.setLoanDate(loanDate);
        
        // 设置每期1天 (模拟按日计息，或者 T+1 还款的场景)
        // 如果是算头不算尾，T日放款，T+1日还款，占用1天
        request.setPeriodDays(1);
        request.setPeriodCount(1);
        // 试算日设为 T+1 (2026-02-02)
        request.setTrialDate(loanDate.plusDays(1));
        
        RepayTrialResult result = service.trial(request);
        
        // 验证结果
        assertNotNull(result.getAmountDetail());
        
        // 预期利息计算:
        // 本金 * 日利率 * 天数
        // 1,000,000 * 0.0005 * 1 = 500
        long expectedInterest = 500L;
        
        // 查找利息科目的金额
        long actualInterest = result.getAmountDetail().stream()
                .filter(d -> d.getSubject() == ChargeSubject.INTEREST)
                .findFirst()
                .map(RepayTrialSubjectDetail::getUnpaidAmount)
                .orElse(-1L);
                
        assertEquals(expectedInterest, actualInterest, "利息应按1天计算 (算头不算尾)");
        
        // 验证日期范围
        assertEquals(loanDate, result.getStartDate());
        assertEquals(loanDate.plusDays(1), result.getDueDate());
    }

    @Test
    public void testTrialRepaidDetailsBoundToPeriod() {
        RepayTrialRequest planRequest = buildRequest(RepayMethod.EQUAL_PRINCIPAL);
        RepayPlanService repayPlanService = buildPlanService();
        List<RepayPlanItem> planItems = repayPlanService.generatePlan(planRequest);
        RepayPlanItem first = planItems.get(0);
        RepayPlanItem second = planItems.get(1);
        long secondPrincipal = getPlanSubjectAmount(second.getAmountDetail(), ChargeSubject.PRINCIPAL);

        RepayTrialScheduleItem period1 = new RepayTrialScheduleItem();
        period1.setPeriod(first.getPeriod());
        period1.setStartDate(first.getStartDate());
        period1.setDueDate(first.getDueDate());
        RepayTrialSubjectAmount repaidPrincipal = new RepayTrialSubjectAmount();
        repaidPrincipal.setSubject(ChargeSubject.PRINCIPAL);
        repaidPrincipal.setAmount(secondPrincipal);
        period1.setRepaidDetails(Collections.singletonList(repaidPrincipal));

        RepayTrialScheduleItem period2 = new RepayTrialScheduleItem();
        period2.setPeriod(second.getPeriod());
        period2.setStartDate(second.getStartDate());
        period2.setDueDate(second.getDueDate());
        period2.setRepaidDetails(new ArrayList<>());

        planRequest.setTrialDate(second.getDueDate());
        planRequest.setPeriodDetails(Arrays.asList(period1, period2));

        RepayTrialService service = buildService();
        RepayTrialResult result = service.trial(planRequest);

        RepayTrialSubjectDetail principalDetail = result.getAmountDetail().stream()
                .filter(detail -> detail.getSubject() == ChargeSubject.PRINCIPAL)
                .findFirst()
                .orElse(null);
        assertNotNull(principalDetail);
        assertEquals(secondPrincipal, principalDetail.getUnpaidAmount());
        assertEquals(0L, principalDetail.getRepaidAmount());
    }

    private RepayTrialService buildService() {
        RepayPlanService repayPlanService = buildPlanService();
        return new RepayTrialServiceImpl(repayPlanService);
    }

    private RepayPlanService buildPlanService() {
        RepayPlanStrategyRouter router = new RepayPlanStrategyRouter(Arrays.asList(
                new EqualPrincipalStrategy(),
                new EqualPrincipalInterestStrategy(),
                new InterestFirstPrincipalLastStrategy()
        ));
        return new RepayPlanServiceImpl(router);
    }

    private RepayTrialRequest buildRequest(RepayMethod method) {
        RepayTrialRequest request = new RepayTrialRequest();
        request.setRepayMethod(method);
        request.setPrincipal(100L);
        request.setDailyRate(1_0000_0000L);
        request.setPenaltyDailyRate(2_0000_0000L);
        request.setLoanDate(LocalDate.of(2026, 2, 1));
        request.setPeriodDays(1);
        request.setPeriodCount(2);
        return request;
    }

    private long getPlanSubjectAmount(List<RepayPlanSubjectDetail> details, ChargeSubject subject) {
        if (details == null || details.isEmpty() || subject == null) {
            return 0L;
        }
        for (RepayPlanSubjectDetail detail : details) {
            if (detail != null && subject == detail.getSubject()) {
                Long amount = detail.getAmount();
                return amount == null ? 0L : amount;
            }
        }
        return 0L;
    }
}
