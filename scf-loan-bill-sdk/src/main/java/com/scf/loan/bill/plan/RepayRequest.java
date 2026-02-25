package com.scf.loan.bill.plan;

import lombok.Data;

@Data
public class RepayRequest extends RepayTrialRequest {
    private Long repayAmount;

    public Long getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(Long repayAmount) {
        this.repayAmount = repayAmount;
    }
}
