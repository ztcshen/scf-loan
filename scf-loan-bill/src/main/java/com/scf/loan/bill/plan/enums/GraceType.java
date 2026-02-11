package com.scf.loan.bill.plan.enums;

public enum GraceType {
    /**
     * 无宽限期
     */
    NONE("NONE");

    private final String code;

    GraceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
