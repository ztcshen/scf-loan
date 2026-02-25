package com.scf.loan.bill.service.impl;

import com.scf.loan.bill.plan.ChargeRate;
import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.bill.plan.RepayTrialRequest;
import com.scf.loan.bill.plan.RepayTrialResult;
import com.scf.loan.bill.plan.RepayTrialScheduleItem;
import com.scf.loan.bill.plan.RepayTrialSubjectAmount;
import com.scf.loan.bill.plan.RepayTrialSubjectDetail;
import com.scf.loan.bill.plan.enums.RateUnit;
import com.scf.loan.bill.service.RepayPlanService;
import com.scf.loan.bill.service.RepayTrialService;
import com.scf.loan.common.enums.ChargeSubject;
import com.scf.loan.common.dto.RepayPlanSubjectDetail;
import com.scf.loan.common.dto.RepayPlanItem;
import com.scf.loan.common.utils.scf.ScfInterestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RepayTrialServiceImpl implements RepayTrialService {
    private final RepayPlanService repayPlanService;

    public RepayTrialServiceImpl(RepayPlanService repayPlanService) {
        this.repayPlanService = repayPlanService;
    }

    @Override
    public RepayTrialResult trial(RepayTrialRequest request) {
        if (request == null || request.getTrialDate() == null) {
            throw new IllegalArgumentException("参数不合法");
        }
        RepayPlanRequest planRequest = request;
        LocalDate trialDate = request.getTrialDate();
        List<RepayTrialScheduleItem> periodDetails = buildScheduleItems(request);
        if (periodDetails == null || periodDetails.isEmpty()) {
            throw new IllegalStateException("还款计划为空");
        }

        RepayTrialScheduleItem current = findCurrentItem(periodDetails, trialDate);
        List<RepayPlanItem> planItems = repayPlanService.generatePlan(planRequest);
        if (planItems == null || planItems.isEmpty()) {
            throw new IllegalStateException("还款计划为空");
        }
        RepayPlanItem planItem = getPlanItem(planItems, current.getPeriod());
        long currentPrincipal = getSubjectAmount(planItem == null ? null : planItem.getAmountDetail(), ChargeSubject.PRINCIPAL);
        long currentInterest = getSubjectAmount(planItem == null ? null : planItem.getAmountDetail(), ChargeSubject.INTEREST);
        long remainingPrincipal = calculateRemainingPrincipal(planItems, planRequest.getPrincipal(), current.getPeriod());
        long repaidPrincipal = getTrialSubjectAmount(current.getRepaidDetails(), ChargeSubject.PRINCIPAL);
        long repaidInterest = getTrialSubjectAmount(current.getRepaidDetails(), ChargeSubject.INTEREST);
        long repaidPenalty = getTrialSubjectAmount(current.getRepaidDetails(), ChargeSubject.PENALTY);
        long unpaidPrincipal = Math.max(0L, currentPrincipal - repaidPrincipal);
        long unpaidInterest = Math.max(0L, currentInterest - repaidInterest);
        long outstandingPrincipal = unpaidPrincipal + remainingPrincipal;

        Map<ChargeSubject, Long> subjectAmounts = new LinkedHashMap<>();
        subjectAmounts.put(ChargeSubject.PRINCIPAL, unpaidPrincipal);
        subjectAmounts.put(ChargeSubject.INTEREST, unpaidInterest);

        int penaltyDays = calculatePenaltyDays(current.getDueDate(), request.getRepayDates(), trialDate);
        long penalty = calculatePenaltyAmount(outstandingPrincipal, planRequest.getPenaltyDailyRate(), penaltyDays);
        long unpaidPenalty = Math.max(0L, penalty - repaidPenalty);
        subjectAmounts.put(ChargeSubject.PENALTY, unpaidPenalty);

        List<ChargeRate> chargeRates = planRequest.getChargeRates();
        if (chargeRates != null) {
            for (ChargeRate chargeRate : chargeRates) {
                if (chargeRate == null || chargeRate.getSubject() == null || chargeRate.getRateUnit() == null) {
                    continue;
                }
                long amount = calculateChargeAmount(outstandingPrincipal, planRequest.getPeriodDays(), chargeRate);
                subjectAmounts.put(chargeRate.getSubject(), subjectAmounts.getOrDefault(chargeRate.getSubject(), 0L) + amount);
            }
        }

        List<RepayTrialSubjectDetail> amountDetail = new ArrayList<>();
        amountDetail.add(buildSubjectDetail(ChargeSubject.PRINCIPAL, currentPrincipal, repaidPrincipal, unpaidPrincipal));
        amountDetail.add(buildSubjectDetail(ChargeSubject.INTEREST, currentInterest, repaidInterest, unpaidInterest));
        amountDetail.add(buildSubjectDetail(ChargeSubject.PENALTY, penalty, repaidPenalty, unpaidPenalty));
        for (Map.Entry<ChargeSubject, Long> entry : subjectAmounts.entrySet()) {
            ChargeSubject subject = entry.getKey();
            if (subject == ChargeSubject.PRINCIPAL || subject == ChargeSubject.INTEREST || subject == ChargeSubject.PENALTY) {
                continue;
            }
            RepayTrialSubjectDetail detail = new RepayTrialSubjectDetail();
            detail.setSubject(subject);
            detail.setDueAmount(0L);
            detail.setRepaidAmount(0L);
            detail.setUnpaidAmount(entry.getValue());
            amountDetail.add(detail);
        }

        RepayTrialResult result = new RepayTrialResult();
        result.setPeriod(current.getPeriod());
        result.setStartDate(current.getStartDate());
        result.setDueDate(current.getDueDate());
        result.setAmountDetail(amountDetail);
        return result;
    }

    private List<RepayTrialScheduleItem> buildScheduleItems(RepayTrialRequest request) {
        List<RepayTrialScheduleItem> periodDetails = request.getPeriodDetails();
        if (periodDetails != null && !periodDetails.isEmpty()) {
            return periodDetails;
        }
        List<RepayPlanItem> items = repayPlanService.generatePlan(request);
        if (items == null || items.isEmpty()) {
            return items == null ? null : new ArrayList<>();
        }
        List<RepayTrialScheduleItem> mapped = new ArrayList<>(items.size());
        for (RepayPlanItem item : items) {
            RepayTrialScheduleItem scheduleItem = new RepayTrialScheduleItem();
            scheduleItem.setPeriod(item.getPeriod());
            scheduleItem.setStartDate(item.getStartDate());
            scheduleItem.setDueDate(item.getDueDate());
            scheduleItem.setRepaidDetails(new ArrayList<>());
            mapped.add(scheduleItem);
        }
        return mapped;
    }

    private RepayPlanItem getPlanItem(List<RepayPlanItem> items, Integer period) {
        if (items == null || items.isEmpty() || period == null) {
            return null;
        }
        for (RepayPlanItem item : items) {
            if (item != null && period.equals(item.getPeriod())) {
                return item;
            }
        }
        return null;
    }

    private long calculateRemainingPrincipal(List<RepayPlanItem> items, Long totalPrincipal, Integer period) {
        long principal = safeLong(totalPrincipal);
        if (items == null || items.isEmpty() || period == null) {
            return principal;
        }
        long paidPrincipal = 0L;
        for (RepayPlanItem item : items) {
            if (item == null || item.getPeriod() == null || item.getPeriod() > period) {
                continue;
            }
            paidPrincipal += getSubjectAmount(item.getAmountDetail(), ChargeSubject.PRINCIPAL);
        }
        long remaining = principal - paidPrincipal;
        return Math.max(0L, remaining);
    }

    private RepayTrialScheduleItem findCurrentItem(List<RepayTrialScheduleItem> items, LocalDate trialDate) {
        RepayTrialScheduleItem first = items.get(0);
        if (trialDate.isBefore(first.getStartDate())) {
            return first;
        }
        RepayTrialScheduleItem last = items.get(items.size() - 1);
        if (trialDate.isAfter(last.getDueDate())) {
            return last;
        }
        for (RepayTrialScheduleItem item : items) {
            if (!trialDate.isBefore(item.getStartDate()) && !trialDate.isAfter(item.getDueDate())) {
                return item;
            }
        }
        return last;
    }

    private int calculatePenaltyDays(LocalDate dueDate, List<LocalDate> repayDates, LocalDate trialDate) {
        if (repayDates == null || repayDates.isEmpty()) {
            return ScfInterestUtils.calculatePenaltyDays(dueDate, trialDate);
        }
        return ScfInterestUtils.calculatePenaltyDays(dueDate, repayDates, trialDate);
    }

    private long calculatePenaltyAmount(long outstandingPrincipal, Long penaltyDailyRate, int penaltyDays) {
        if (penaltyDays <= 0 || penaltyDailyRate == null || penaltyDailyRate <= 0) {
            return 0L;
        }
        BigDecimal rate = ScfInterestUtils.convertRateToBigDecimal(penaltyDailyRate);
        return ScfInterestUtils.calculateUnpaidInterest(outstandingPrincipal, rate, penaltyDays);
    }

    private long calculateChargeAmount(long outstandingPrincipal, Integer periodDays, ChargeRate chargeRate) {
        long rateValue = safeLong(chargeRate.getRateValue());
        if (outstandingPrincipal <= 0 || rateValue <= 0) {
            return 0L;
        }
        BigDecimal base = BigDecimal.valueOf(outstandingPrincipal);
        long periodDaysValue = periodDays == null ? 0L : periodDays.longValue();
        BigDecimal rate = ScfInterestUtils.convertRateToBigDecimal(rateValue);
        BigDecimal amount;
        if (chargeRate.getRateUnit() == RateUnit.DAILY) {
            amount = base.multiply(rate).multiply(BigDecimal.valueOf(periodDaysValue));
        } else if (chargeRate.getRateUnit() == RateUnit.YEARLY) {
            BigDecimal yearRatio = BigDecimal.valueOf(periodDaysValue)
                    .divide(BigDecimal.valueOf(365), 12, RoundingMode.DOWN);
            amount = base.multiply(rate).multiply(yearRatio);
        } else {
            amount = base.multiply(rate);
        }
        return amount.setScale(0, RoundingMode.DOWN).longValue();
    }

    private long safeLong(Long value) {
        return value == null ? 0L : value;
    }

    private long getSubjectAmount(List<RepayPlanSubjectDetail> details, ChargeSubject subject) {
        if (details == null || details.isEmpty() || subject == null) {
            return 0L;
        }
        for (RepayPlanSubjectDetail detail : details) {
            if (detail != null && subject == detail.getSubject()) {
                return safeLong(detail.getAmount());
            }
        }
        return 0L;
    }

    private long getTrialSubjectAmount(List<RepayTrialSubjectAmount> details, ChargeSubject subject) {
        if (details == null || details.isEmpty() || subject == null) {
            return 0L;
        }
        for (RepayTrialSubjectAmount detail : details) {
            if (detail != null && subject == detail.getSubject()) {
                return safeLong(detail.getAmount());
            }
        }
        return 0L;
    }

    private RepayTrialSubjectDetail buildSubjectDetail(ChargeSubject subject, long dueAmount, long repaidAmount, long unpaidAmount) {
        RepayTrialSubjectDetail detail = new RepayTrialSubjectDetail();
        detail.setSubject(subject);
        detail.setDueAmount(dueAmount);
        detail.setRepaidAmount(repaidAmount);
        detail.setUnpaidAmount(unpaidAmount);
        return detail;
    }
}
