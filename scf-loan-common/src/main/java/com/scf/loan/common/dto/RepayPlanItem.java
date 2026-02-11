package com.scf.loan.common.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RepayPlanItem {
    /**
     * 期次序号，从 1 开始递增。
     */
    private Integer period;
    /**
     * 本期起始日期。
     */
    private LocalDate startDate;
    /**
     * 本期到期日期。
     */
    private LocalDate dueDate;
    /**
     * 本期应还本金，单位：分。
     */
    private Long principal;
    /**
     * 本期应还利息，单位：分。
     */
    private Long interest;
    /**
     * 本期应还总额，单位：分。
     */
    private Long total;
    /**
     * 本期结束后的剩余本金，单位：分。
     */
    private Long remainingPrincipal;
}
