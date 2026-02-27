package com.scf.loan.bill.plan;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RepayTrialResult {
    private Integer period;
    private LocalDate startDate;
    private LocalDate dueDate;
    /**
     * 逾期天数
     */
    private Integer overdueDays;
    /**
     * 计息天数
     */
    private Integer interestDays;
    private List<RepayTrialSubjectDetail> amountDetail;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public Integer getInterestDays() {
        return interestDays;
    }

    public void setInterestDays(Integer interestDays) {
        this.interestDays = interestDays;
    }

    public List<RepayTrialSubjectDetail> getAmountDetail() {
        return amountDetail;
    }

    public void setAmountDetail(List<RepayTrialSubjectDetail> amountDetail) {
        this.amountDetail = amountDetail;
    }
}
