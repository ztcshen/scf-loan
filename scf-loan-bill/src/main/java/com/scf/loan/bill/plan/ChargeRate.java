package com.scf.loan.bill.plan;

import com.scf.loan.bill.plan.enums.ChargeSubject;
import com.scf.loan.bill.plan.enums.RateUnit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ChargeRate {
    @NotNull
    private ChargeSubject subject;
    @NotNull
    private RateUnit rateUnit;
    @NotNull
    @Positive
    private Long rateValue;

    public ChargeSubject getSubject() {
        return subject;
    }

    public void setSubject(ChargeSubject subject) {
        this.subject = subject;
    }

    public RateUnit getRateUnit() {
        return rateUnit;
    }

    public void setRateUnit(RateUnit rateUnit) {
        this.rateUnit = rateUnit;
    }

    public Long getRateValue() {
        return rateValue;
    }

    public void setRateValue(Long rateValue) {
        this.rateValue = rateValue;
    }
}
