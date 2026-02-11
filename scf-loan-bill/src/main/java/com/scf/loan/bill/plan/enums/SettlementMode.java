package com.scf.loan.bill.plan.enums;

public enum SettlementMode {
    /**
     * 到期结算
     */
    ON_DUE("ON_DUE");

    private final String code;

    SettlementMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
