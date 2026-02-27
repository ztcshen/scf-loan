package com.scf.loan.bill.plan;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class RepayTrialRequest extends RepayPlanRequest {
    @NotNull
    private LocalDate trialDate;
    private List<LocalDate> repayDates;
    private List<RepayTrialScheduleItem> periodDetails;
}
