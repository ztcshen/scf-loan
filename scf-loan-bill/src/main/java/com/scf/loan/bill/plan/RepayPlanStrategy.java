package com.scf.loan.bill.plan;

import com.scf.loan.common.dto.RepayPlanItem;

import java.util.List;

public interface RepayPlanStrategy {
    RepayPlanStrategyKey key();

    List<RepayPlanItem> generate(RepayPlanRequest request);
}
