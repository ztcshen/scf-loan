package com.scf.loan.bill.plan;

import com.scf.loan.bill.plan.enums.RepayMethod;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Data
public class RepayPlanRequest {
    @NotNull
    private RepayMethod repayMethod;

    @NotNull
    @Positive
    private Long principal;

    @NotNull
    @Positive
    private Long dailyRate;

    @NotNull
    @Positive
    private Long penaltyDailyRate;

    @NotNull
    private LocalDate loanDate;

    @NotNull
    @Positive
    private Integer periodDays;

    @NotNull
    @Positive
    private Integer periodCount;

    private String interestType;
    private String periodUnit;
    private String dayCountConvention;
    private String rateType;
    private String graceType;
    private String settlementMode;
    private List<ChargeRate> chargeRates;
}
