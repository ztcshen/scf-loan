package com.scf.loan.bill.plan.enums;

public enum RateType {
    FIXED("FIXED"),
    FLOAT("FLOAT");

    private final String code;

    RateType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
