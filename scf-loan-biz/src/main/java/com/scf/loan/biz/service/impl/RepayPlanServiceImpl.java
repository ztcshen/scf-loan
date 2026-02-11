package com.scf.loan.biz.service.impl;

import com.scf.loan.biz.service.RepayPlanService;
import com.scf.loan.common.dto.RepayPlanItem;
import com.scf.loan.common.dto.RepayPlanSubjectDetail;
import com.scf.loan.common.enums.ChargeSubject;
import com.scf.loan.common.utils.scf.ScfInterestUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepayPlanServiceImpl implements RepayPlanService {

    @Override
    public List<RepayPlanItem> generateEqualPrincipalPlan(long principal,
                                                          long dailyRate,
                                                          LocalDate loanDate,
                                                          int periodDays,
                                                          int periodCount) {
        if (principal <= 0 || dailyRate <= 0 || loanDate == null || periodDays <= 0 || periodCount <= 0) {
            throw new IllegalArgumentException("参数不合法");
        }

        List<RepayPlanItem> items = new ArrayList<>(periodCount);
        long perPrincipal = principal / periodCount;
        long remainingPrincipal = principal;
        LocalDate startDate = loanDate;

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
