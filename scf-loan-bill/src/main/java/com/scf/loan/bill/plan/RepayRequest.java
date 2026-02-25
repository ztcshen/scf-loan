package com.scf.loan.bill.plan;

import lombok.Data;

@Data
public class RepayRequest extends RepayTrialRequest {
    /**
     * 本次还款金额，单位：分
     */
    private Long repayAmount;

    public Long getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(Long repayAmount) {
        this.repayAmount = repayAmount;
    }
}
