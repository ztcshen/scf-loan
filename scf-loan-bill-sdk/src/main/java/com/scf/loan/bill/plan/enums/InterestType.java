package com.scf.loan.bill.plan.enums;

public enum InterestType {
    DAILY("D");

    private final String code;

    InterestType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
