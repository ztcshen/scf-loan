package com.scf.loan.bill.plan;

import com.scf.loan.common.enums.ChargeSubject;

public class ChargeAmount {
    private ChargeSubject subject;
    private Long amount;

    public ChargeSubject getSubject() {
        return subject;
    }

    public void setSubject(ChargeSubject subject) {
        this.subject = subject;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
