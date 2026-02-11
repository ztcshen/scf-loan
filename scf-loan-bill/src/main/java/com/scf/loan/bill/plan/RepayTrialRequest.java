package com.scf.loan.bill.plan;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RepayTrialRequest {
    /**
     * 还款计划生成参数
     */
    private RepayPlanRequest planRequest;
    /**
     * 试算日期
     */
    private LocalDate trialDate;
    /**
     * 历史还款日期列表，用于计算利息/罚息天数。
     * <p>
     * <b>注意：</b> 此逻辑假设最近一次还款日之前的利息/罚息已完全结清。
     * 如果存在“部分还款但未结清利息”的情况，单纯依赖此日期截断会导致历史未还利息丢失。
     * 调用方需确保传入的 {@code periodDetails} 或外部逻辑已包含历史欠费，或者仅在利息结清时才更新此列表。
     * </p>
     */
    private List<LocalDate> repayDates;
    /**
     * 各期科目已还/应还数据
     */
    private List<RepayTrialScheduleItem> periodDetails;

    public RepayPlanRequest getPlanRequest() {
        return planRequest;
    }

    public void setPlanRequest(RepayPlanRequest planRequest) {
        this.planRequest = planRequest;
    }

    public LocalDate getTrialDate() {
        return trialDate;
    }

    public void setTrialDate(LocalDate trialDate) {
        this.trialDate = trialDate;
    }

    public List<LocalDate> getRepayDates() {
        return repayDates;
    }

    public void setRepayDates(List<LocalDate> repayDates) {
        this.repayDates = repayDates;
    }

    public List<RepayTrialScheduleItem> getPeriodDetails() {
        return periodDetails;
    }

    public void setPeriodDetails(List<RepayTrialScheduleItem> periodDetails) {
        this.periodDetails = periodDetails;
    }
}
