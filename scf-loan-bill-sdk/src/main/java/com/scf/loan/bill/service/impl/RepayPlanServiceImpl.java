package com.scf.loan.bill.service.impl;

import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.bill.plan.RepayPlanStrategyKey;
import com.scf.loan.bill.plan.RepayPlanStrategyRouter;
import com.scf.loan.bill.service.RepayPlanService;
import com.scf.loan.common.dto.RepayPlanItem;

import java.util.List;

public class RepayPlanServiceImpl implements RepayPlanService {
    private final RepayPlanStrategyRouter router;

    public RepayPlanServiceImpl(RepayPlanStrategyRouter router) {
        this.router = router;
    }

    @Override
    public List<RepayPlanItem> generatePlan(RepayPlanRequest request) {
        RepayPlanStrategyKey key = new RepayPlanStrategyKey();
        key.setRepayMethod(request.getRepayMethod());
        key.setInterestType(request.getInterestType());
        key.setPeriodUnit(request.getPeriodUnit());
        key.setDayCountConvention(request.getDayCountConvention());
        key.setRateType(request.getRateType());
        key.setGraceType(request.getGraceType());
        key.setSettlementMode(request.getSettlementMode());
        return router.route(key).generate(request);
    }
}
