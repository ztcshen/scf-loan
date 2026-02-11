package com.scf.loan.biz.service;

import com.scf.loan.common.dto.RepayPlanItem;

import java.time.LocalDate;
import java.util.List;

public interface RepayPlanService {
    List<RepayPlanItem> generateEqualPrincipalPlan(long principal,
                                                   long dailyRate,
                                                   LocalDate loanDate,
                                                   int periodDays,
                                                   int periodCount);
}
