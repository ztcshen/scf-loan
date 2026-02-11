package com.scf.loan.common.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RepayPlanItem {
    private Integer period;
    private LocalDate startDate;
    private LocalDate dueDate;
    private List<RepayPlanSubjectDetail> amountDetail;
}
