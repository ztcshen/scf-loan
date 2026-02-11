package com.scf.loan.common.utils.scf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScfInterestUtils {

    public static int calculateInterestDays(LocalDate startDate, LocalDate queryDate) {
        if (startDate == null || queryDate == null) {
            throw new IllegalArgumentException("startDate 和 queryDate 都不能为空");
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, queryDate);
        return (int) Math.max(daysBetween, 1);
    }

    public static int calculateInterestDays(LocalDate startDate, List<LocalDate> repayDateList, LocalDate queryDate) {
        if (startDate == null || queryDate == null) {
            throw new IllegalArgumentException("startDate 和 queryDate 都不能为空");
        }

        List<LocalDate> previousRepayDates = Optional.ofNullable(repayDateList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(date -> date != null && !date.isAfter(queryDate))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (previousRepayDates.isEmpty()) {
            return calculateInterestDays(startDate, queryDate);
        }

        LocalDate latestRepayDate = previousRepayDates.get(0);
        long daysBetween = ChronoUnit.DAYS.between(latestRepayDate, queryDate);
        return (int) daysBetween;
    }

    public static int calculatePenaltyDays(LocalDate dueDate, LocalDate queryDate) {
        if (queryDate.isAfter(dueDate)) {
            return (int) ChronoUnit.DAYS.between(dueDate, queryDate);
        }

        return 0;
    }

    public static int calculatePenaltyDays(LocalDate dueDate, List<LocalDate> repayDateList, LocalDate queryDate) {
        if (dueDate == null || queryDate == null) {
            throw new IllegalArgumentException("dueDate 和 queryDate 都不能为空");
        }

        List<LocalDate> previousRepayDates = Optional.ofNullable(repayDateList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(date -> date != null && !date.isAfter(queryDate))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (previousRepayDates.isEmpty()) {
            return calculatePenaltyDays(dueDate, queryDate);
        }

        LocalDate latestRepayDate = previousRepayDates.get(0);
        LocalDate penaltyStartDate = latestRepayDate.isAfter(dueDate) ? latestRepayDate : dueDate;
        return calculatePenaltyDays(penaltyStartDate, queryDate);
    }

    public static long calculateInterest(Long principal, Long rate, Long days) {
        if (principal == null || rate == null || principal <= 0 || rate <= 0 || days <= 0) {
            return 0L;
        }

        BigDecimal principalBigDecimal = BigDecimal.valueOf(principal);
        BigDecimal rateBigDecimal = BigDecimal.valueOf(rate);

        BigDecimal interest = principalBigDecimal.multiply(rateBigDecimal)
                .divide(BigDecimal.valueOf(1_0000_0000L), 12, RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(days))
                .setScale(0, RoundingMode.DOWN);

        return interest.longValue();
    }

    public static BigDecimal convertRateToBigDecimal(Long rate) {
        return Optional.ofNullable(rate)
                .map(BigDecimal::valueOf)
                .orElse(BigDecimal.ZERO)
                .divide(BigDecimal.valueOf(1_0000_0000L), 12, RoundingMode.DOWN);
    }

    public static long calculateUnpaidInterest(Long unpaidPrincipal, BigDecimal dailyRate, int interestDays) {
        if (unpaidPrincipal == null || unpaidPrincipal <= 0 || interestDays <= 0) {
            return 0L;
        }

        BigDecimal principal = BigDecimal.valueOf(unpaidPrincipal);
        BigDecimal unpaidInterest = principal.multiply(dailyRate)
                .multiply(BigDecimal.valueOf(interestDays))
                .setScale(0, RoundingMode.DOWN);

        return unpaidInterest.longValue();
    }
}
