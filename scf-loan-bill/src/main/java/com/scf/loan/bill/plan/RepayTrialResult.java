package com.scf.loan.bill.plan;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RepayTrialResult {
    /**
     * 期次序号
     */
    private Integer period;
    /**
     * 本期起始日期
     */
    private LocalDate startDate;
    /**
     * 本期到期日期
     */
    private LocalDate dueDate;
    /**
     * 各科目应还/已还/未还金额明细
     */
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

    public List<RepayTrialSubjectDetail> getAmountDetail() {
        return amountDetail;
    }

    public void setAmountDetail(List<RepayTrialSubjectDetail> amountDetail) {
        this.amountDetail = amountDetail;
    }
}
