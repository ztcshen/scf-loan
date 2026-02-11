package com.scf.loan.bill.service;

import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.common.dto.RepayPlanItem;

import java.util.List;

public interface RepayPlanService {
    List<RepayPlanItem> generatePlan(RepayPlanRequest request);
}
