package com.scf.loan.bill.plan;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RepayTrialScheduleItem {
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
     * 本期已还科目明细
     */
    private List<RepayTrialSubjectAmount> repaidDetails;

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

    public List<RepayTrialSubjectAmount> getRepaidDetails() {
        return repaidDetails;
    }

    public void setRepaidDetails(List<RepayTrialSubjectAmount> repaidDetails) {
        this.repaidDetails = repaidDetails;
    }
}
