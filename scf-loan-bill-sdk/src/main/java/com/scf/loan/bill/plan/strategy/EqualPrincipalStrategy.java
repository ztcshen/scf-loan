package com.scf.loan.bill.plan.strategy;

import com.scf.loan.bill.plan.enums.RepayMethod;
import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.bill.plan.RepayPlanStrategy;
import com.scf.loan.bill.plan.RepayPlanStrategyKey;
import com.scf.loan.common.dto.RepayPlanItem;
import com.scf.loan.common.dto.RepayPlanSubjectDetail;
import com.scf.loan.common.enums.ChargeSubject;
import com.scf.loan.common.utils.scf.ScfInterestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EqualPrincipalStrategy extends BaseRepayPlanStrategy implements RepayPlanStrategy {
    @Override
    public RepayPlanStrategyKey key() {
        RepayPlanStrategyKey key = new RepayPlanStrategyKey();
        key.setRepayMethod(RepayMethod.EQUAL_PRINCIPAL);
        return key;
    }

    @Override
    public List<RepayPlanItem> generate(RepayPlanRequest request) {
        validate(request);
        int periodCount = request.getPeriodCount();
        int periodDays = request.getPeriodDays();
        long principal = request.getPrincipal();
        long dailyRate = request.getDailyRate();

        List<RepayPlanItem> items = new ArrayList<>(periodCount);
        long perPrincipal = principal / periodCount;
        long remainingPrincipal = principal;
        LocalDate startDate = request.getLoanDate();

        for (int period = 1; period <= periodCount; period++) {
            long currentPrincipal = period == periodCount ? remainingPrincipal : perPrincipal;
            long interest = ScfInterestUtils.calculateInterest(remainingPrincipal, dailyRate, (long) periodDays);
            remainingPrincipal -= currentPrincipal;
            LocalDate dueDate = startDate.plusDays(periodDays);

            RepayPlanItem item = new RepayPlanItem();
            item.setPeriod(period);
            item.setStartDate(startDate);
            item.setDueDate(dueDate);
            List<RepayPlanSubjectDetail> amountDetail = new ArrayList<>(2);
            RepayPlanSubjectDetail principalDetail = new RepayPlanSubjectDetail();
            principalDetail.setSubject(ChargeSubject.PRINCIPAL);
            principalDetail.setAmount(currentPrincipal);
            amountDetail.add(principalDetail);
            RepayPlanSubjectDetail interestDetail = new RepayPlanSubjectDetail();
            interestDetail.setSubject(ChargeSubject.INTEREST);
            interestDetail.setAmount(interest);
            amountDetail.add(interestDetail);
            item.setAmountDetail(amountDetail);
            items.add(item);

            startDate = dueDate;
        }

        return items;
    }
}
