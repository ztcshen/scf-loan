package com.scf.loan.bill.plan.enums;

public enum DayCountConvention {
    ACT_365("ACT365");

    private final String code;

    DayCountConvention(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
