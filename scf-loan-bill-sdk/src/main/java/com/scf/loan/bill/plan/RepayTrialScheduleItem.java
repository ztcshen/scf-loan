package com.scf.loan.bill.plan;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RepayTrialScheduleItem {
    private Integer period;
    private LocalDate startDate;
    private LocalDate dueDate;
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
