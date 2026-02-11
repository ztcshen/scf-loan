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
     * 历史还款日期列表，用于计算利息/罚息天数
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
