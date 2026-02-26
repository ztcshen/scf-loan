package com.scf.loan.job.framework.model;

public class RouteResult {
    private final String routeKey;
    private final boolean hit;
    private final String strategyName;

    public RouteResult(String routeKey, boolean hit, String strategyName) {
        this.routeKey = routeKey;
        this.hit = hit;
        this.strategyName = strategyName;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public boolean isHit() {
        return hit;
    }

    public String getStrategyName() {
        return strategyName;
    }
}
