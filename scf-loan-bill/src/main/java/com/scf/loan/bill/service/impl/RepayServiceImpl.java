package com.scf.loan.bill.service.impl;

import com.scf.loan.bill.plan.RepayRequest;
import com.scf.loan.bill.plan.RepayResult;
import com.scf.loan.bill.plan.RepayTrialResult;
import com.scf.loan.bill.plan.RepayTrialSubjectAmount;
import com.scf.loan.bill.plan.RepayTrialSubjectDetail;
import com.scf.loan.bill.service.RepayService;
import com.scf.loan.bill.service.RepayTrialService;
import com.scf.loan.common.enums.ChargeSubject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepayServiceImpl implements RepayService {
    private final RepayTrialService repayTrialService;

    public RepayServiceImpl(RepayTrialService repayTrialService) {
        this.repayTrialService = repayTrialService;
    }

    @Override
    public RepayResult repay(RepayRequest request) {
        if (request == null || request.getTrialDate() == null || request.getRepayAmount() == null || request.getRepayAmount() <= 0) {
            throw new IllegalArgumentException("参数不合法");
        }
        RepayTrialResult trialResult = repayTrialService.trial(request);
        List<RepayTrialSubjectDetail> details = trialResult.getAmountDetail();
        if (details == null || details.isEmpty()) {
            throw new IllegalStateException("还款试算结果为空");
        }

        List<RepayTrialSubjectDetail> updatedDetails = copyDetails(details);
        Map<ChargeSubject, RepayTrialSubjectDetail> detailMap = mapDetails(updatedDetails);
        long repayAmount = request.getRepayAmount();
        long totalUnpaid = sumUnpaid(updatedDetails);
        if (repayAmount > totalUnpaid) {
            throw new IllegalArgumentException("还款金额不能超过试算金额");
        }
        long unpaidInterest = getUnpaidAmount(detailMap.get(ChargeSubject.INTEREST));
        if (unpaidInterest > repayAmount) {
            throw new IllegalArgumentException("当期剩余利息需结清");
        }

        Map<ChargeSubject, Long> paidMap = new LinkedHashMap<>();
        long remaining = repayAmount;
        List<ChargeSubject> priorities = Arrays.asList(ChargeSubject.INTEREST, ChargeSubject.PENALTY, ChargeSubject.PRINCIPAL);
        for (ChargeSubject subject : priorities) {
            remaining = applyPayment(detailMap.get(subject), remaining, paidMap);
        }
        for (RepayTrialSubjectDetail detail : updatedDetails) {
            if (detail == null || detail.getSubject() == null || priorities.contains(detail.getSubject())) {
                continue;
            }
            remaining = applyPayment(detail, remaining, paidMap);
        }

        RepayResult result = new RepayResult();
        result.setPeriod(trialResult.getPeriod());
        result.setStartDate(trialResult.getStartDate());
        result.setDueDate(trialResult.getDueDate());
        result.setRepayAmount(repayAmount);
        result.setRemainingAmount(remaining);
        result.setAmountDetail(updatedDetails);
        result.setRepayDetails(buildPaidDetails(paidMap));
        return result;
    }

    private List<RepayTrialSubjectDetail> copyDetails(List<RepayTrialSubjectDetail> details) {
        List<RepayTrialSubjectDetail> copied = new ArrayList<>(details.size());
        for (RepayTrialSubjectDetail detail : details) {
            if (detail == null) {
                continue;
            }
            RepayTrialSubjectDetail clone = new RepayTrialSubjectDetail();
            clone.setSubject(detail.getSubject());
            clone.setDueAmount(detail.getDueAmount());
            clone.setRepaidAmount(detail.getRepaidAmount());
            clone.setUnpaidAmount(detail.getUnpaidAmount());
            copied.add(clone);
        }
        return copied;
    }

    private Map<ChargeSubject, RepayTrialSubjectDetail> mapDetails(List<RepayTrialSubjectDetail> details) {
        Map<ChargeSubject, RepayTrialSubjectDetail> map = new LinkedHashMap<>();
        for (RepayTrialSubjectDetail detail : details) {
            if (detail == null || detail.getSubject() == null) {
                continue;
            }
            map.put(detail.getSubject(), detail);
        }
        return map;
    }

    private long sumUnpaid(List<RepayTrialSubjectDetail> details) {
        if (details == null || details.isEmpty()) {
            return 0L;
        }
        long total = 0L;
        for (RepayTrialSubjectDetail detail : details) {
            total += getUnpaidAmount(detail);
        }
        return total;
    }

    private long applyPayment(RepayTrialSubjectDetail detail, long remaining, Map<ChargeSubject, Long> paidMap) {
        if (detail == null || detail.getSubject() == null || remaining <= 0) {
            return remaining;
        }
        long unpaid = getUnpaidAmount(detail);
        if (unpaid <= 0) {
            return remaining;
        }
        long payment = Math.min(remaining, unpaid);
        detail.setRepaidAmount(safeLong(detail.getRepaidAmount()) + payment);
        detail.setUnpaidAmount(unpaid - payment);
        paidMap.put(detail.getSubject(), paidMap.getOrDefault(detail.getSubject(), 0L) + payment);
        return remaining - payment;
    }

    private List<RepayTrialSubjectAmount> buildPaidDetails(Map<ChargeSubject, Long> paidMap) {
        if (paidMap.isEmpty()) {
            return new ArrayList<>();
        }
        List<RepayTrialSubjectAmount> items = new ArrayList<>(paidMap.size());
        for (Map.Entry<ChargeSubject, Long> entry : paidMap.entrySet()) {
            RepayTrialSubjectAmount amount = new RepayTrialSubjectAmount();
            amount.setSubject(entry.getKey());
            amount.setAmount(entry.getValue());
            items.add(amount);
        }
        return items;
    }

    private long getUnpaidAmount(RepayTrialSubjectDetail detail) {
        if (detail == null || detail.getUnpaidAmount() == null) {
            return 0L;
        }
        return detail.getUnpaidAmount();
    }

    private long safeLong(Long value) {
        return value == null ? 0L : value;
    }
}
