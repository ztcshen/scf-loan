package com.scf.loan.bill.plan.enums;

public enum RateType {
    /**
     * 固定利率
     */
    FIXED("FIXED"),
    /**
     * 浮动利率
     */
    FLOAT("FLOAT");

    private final String code;

    RateType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
