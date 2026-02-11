package com.scf.loan.bill.plan.enums;

public enum DayCountConvention {
    /**
     * 按 ACT/365 计息天数约定
     */
    ACT_365("ACT365");

    private final String code;

    DayCountConvention(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
