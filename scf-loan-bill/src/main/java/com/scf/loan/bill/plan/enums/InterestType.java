package com.scf.loan.bill.plan.enums;

public enum InterestType {
    /**
     * 按日计息
     */
    DAILY("D");

    private final String code;

    InterestType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
