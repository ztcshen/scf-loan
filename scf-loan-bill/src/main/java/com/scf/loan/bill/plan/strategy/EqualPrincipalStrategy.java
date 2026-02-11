package com.scf.loan.bill.plan.strategy;

import com.scf.loan.bill.plan.enums.RepayMethod;
import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.bill.plan.RepayPlanStrategy;
import com.scf.loan.bill.plan.RepayPlanStrategyKey;
import com.scf.loan.common.dto.RepayPlanItem;
import com.scf.loan.common.utils.scf.ScfInterestUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class EqualPrincipalStrategy extends BaseRepayPlanStrategy implements RepayPlanStrategy {
    @Override
    public RepayPlanStrategyKey key() {
        return RepayPlanStrategyKey.builder().repayMethod(RepayMethod.EQUAL_PRINCIPAL).build();
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
            long total = currentPrincipal + interest;
            remainingPrincipal -= currentPrincipal;
            LocalDate dueDate = startDate.plusDays(periodDays);

            RepayPlanItem item = new RepayPlanItem();
            item.setPeriod(period);
            item.setStartDate(startDate);
            item.setDueDate(dueDate);
            item.setPrincipal(currentPrincipal);
            item.setInterest(interest);
            item.setTotal(total);
            item.setRemainingPrincipal(remainingPrincipal);
            items.add(item);

            startDate = dueDate;
        }

        return items;
    }
}
