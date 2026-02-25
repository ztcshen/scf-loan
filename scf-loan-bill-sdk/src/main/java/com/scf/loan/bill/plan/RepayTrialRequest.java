package com.scf.loan.bill.plan;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RepayTrialRequest extends RepayPlanRequest {
    private LocalDate trialDate;
    private List<LocalDate> repayDates;
    private List<RepayTrialScheduleItem> periodDetails;

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
