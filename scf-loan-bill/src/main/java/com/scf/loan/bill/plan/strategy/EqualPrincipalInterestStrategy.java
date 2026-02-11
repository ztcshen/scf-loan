package com.scf.loan.bill.plan.strategy;

import com.scf.loan.bill.plan.enums.RepayMethod;
import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.bill.plan.RepayPlanStrategy;
import com.scf.loan.bill.plan.RepayPlanStrategyKey;
import com.scf.loan.common.dto.RepayPlanItem;
import com.scf.loan.common.utils.scf.ScfInterestUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class EqualPrincipalInterestStrategy extends BaseRepayPlanStrategy implements RepayPlanStrategy {
    @Override
    public RepayPlanStrategyKey key() {
        return RepayPlanStrategyKey.builder().repayMethod(RepayMethod.EQUAL_PRINCIPAL_INTEREST).build();
    }

    @Override
    public List<RepayPlanItem> generate(RepayPlanRequest request) {
        validate(request);
        int periodCount = request.getPeriodCount();
        int periodDays = request.getPeriodDays();
        long principal = request.getPrincipal();

        BigDecimal dailyRate = ScfInterestUtils.convertRateToBigDecimal(request.getDailyRate());
        BigDecimal periodRate = dailyRate.multiply(BigDecimal.valueOf(periodDays));
        BigDecimal factor = BigDecimal.ONE.add(periodRate).pow(periodCount);
        BigDecimal numerator = BigDecimal.valueOf(principal).multiply(periodRate).multiply(factor);
        BigDecimal denominator = factor.subtract(BigDecimal.ONE);
        long installmentTotal = numerator.divide(denominator, 12, RoundingMode.DOWN)
                .setScale(0, RoundingMode.DOWN)
                .longValue();

        List<RepayPlanItem> items = new ArrayList<>(periodCount);
        long remainingPrincipal = principal;
        LocalDate startDate = request.getLoanDate();

        for (int period = 1; period <= periodCount; period++) {
            BigDecimal interestDecimal = BigDecimal.valueOf(remainingPrincipal).multiply(periodRate)
                    .setScale(0, RoundingMode.DOWN);
            long interest = interestDecimal.longValue();
            long currentPrincipal;
            long total;
            if (period == periodCount) {
                currentPrincipal = remainingPrincipal;
                total = currentPrincipal + interest;
            } else {
                total = installmentTotal;
                currentPrincipal = total - interest;
            }
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
