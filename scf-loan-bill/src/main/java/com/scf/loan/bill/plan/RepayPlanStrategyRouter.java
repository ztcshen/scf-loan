package com.scf.loan.bill.plan;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RepayPlanStrategyRouter {
    private final Map<RepayPlanStrategyKey, RepayPlanStrategy> registry = new HashMap<>();

    public RepayPlanStrategyRouter(List<RepayPlanStrategy> strategies) {
        for (RepayPlanStrategy strategy : strategies) {
            RepayPlanStrategyKey key = strategy.key();
            if (registry.containsKey(key)) {
                throw new IllegalStateException("策略重复: " + key);
            }
            registry.put(key, strategy);
        }
    }

    public RepayPlanStrategy route(RepayPlanStrategyKey key) {
        RepayPlanStrategy strategy = registry.get(key);
        if (strategy != null) {
            return strategy;
        }
        RepayPlanStrategy fallback = registry.get(key.fallback());
        if (fallback != null) {
            return fallback;
        }
        throw new IllegalArgumentException("未找到还款计划策略: " + key);
    }
}
