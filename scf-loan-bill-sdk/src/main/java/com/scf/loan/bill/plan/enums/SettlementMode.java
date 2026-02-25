package com.scf.loan.bill.plan.enums;

public enum SettlementMode {
    ON_DUE("ON_DUE");

    private final String code;

    SettlementMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
