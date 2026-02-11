package com.scf.loan.bill.plan.enums;

public enum PeriodUnit {
    /**
     * 天
     */
    DAY("DAY");

    private final String code;

    PeriodUnit(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
