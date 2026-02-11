package com.scf.loan.common.dto;

import com.scf.loan.common.enums.ChargeSubject;
import lombok.Data;

@Data
public class RepayPlanSubjectDetail {
    /**
     * 科目标识
     */
    private ChargeSubject subject;
    /**
     * 本期应还金额
     */
    private Long amount;
}
