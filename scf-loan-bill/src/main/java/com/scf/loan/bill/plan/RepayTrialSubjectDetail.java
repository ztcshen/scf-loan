package com.scf.loan.bill.plan;

import com.scf.loan.common.enums.ChargeSubject;
import lombok.Data;

@Data
public class RepayTrialSubjectDetail {
    /**
     * 科目标识
     */
    private ChargeSubject subject;
    /**
     * 本期应还金额
     */
    private Long dueAmount;
    /**
     * 本期已还金额
     */
    private Long repaidAmount;
    /**
     * 本期未还金额
     */
    private Long unpaidAmount;

    public ChargeSubject getSubject() {
        return subject;
    }

    public void setSubject(ChargeSubject subject) {
        this.subject = subject;
    }

    public Long getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Long dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Long getRepaidAmount() {
        return repaidAmount;
    }

    public void setRepaidAmount(Long repaidAmount) {
        this.repaidAmount = repaidAmount;
    }

    public Long getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(Long unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }
}
