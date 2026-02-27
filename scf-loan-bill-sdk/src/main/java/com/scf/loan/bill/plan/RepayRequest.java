package com.scf.loan.bill.plan;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class RepayRequest extends RepayTrialRequest {
    @NotNull
    @Positive
    private Long repayAmount;
}
