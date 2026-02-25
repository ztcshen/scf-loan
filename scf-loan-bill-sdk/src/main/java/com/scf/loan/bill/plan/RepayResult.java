package com.scf.loan.bill.plan;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RepayResult {
    private Integer period;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Long repayAmount;
    private Long remainingAmount;
    private List<RepayTrialSubjectDetail> amountDetail;
    private List<RepayTrialSubjectAmount> repayDetails;

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

    public Long getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(Long repayAmount) {
        this.repayAmount = repayAmount;
    }

    public Long getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Long remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public List<RepayTrialSubjectDetail> getAmountDetail() {
        return amountDetail;
    }

    public void setAmountDetail(List<RepayTrialSubjectDetail> amountDetail) {
        this.amountDetail = amountDetail;
    }

    public List<RepayTrialSubjectAmount> getRepayDetails() {
        return repayDetails;
    }

    public void setRepayDetails(List<RepayTrialSubjectAmount> repayDetails) {
        this.repayDetails = repayDetails;
    }
}
