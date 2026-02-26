package com.scf.loan.job.framework.route;

import com.scf.loan.job.framework.model.RouteResult;
import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RouteService {
    private final List<RouteStrategy> strategies;

    @Autowired
    public RouteService(List<RouteStrategy> strategies) {
        if (strategies == null) {
            this.strategies = new ArrayList<>();
        } else {
            this.strategies = new ArrayList<>(strategies);
            this.strategies.sort(Comparator.comparingInt(RouteStrategy::getOrder));
        }
    }

    public RouteResult route(ScfCoreCommandParam<?> param) {
        for (RouteStrategy strategy : strategies) {
            if (!strategy.supports(param)) {
                continue;
            }
            String routeKey = strategy.route(param);
            if (routeKey != null && !routeKey.isBlank()) {
                param.setRouteKey(routeKey);
                return new RouteResult(routeKey, true, strategy.getClass().getSimpleName());
            }
        }
        return new RouteResult(param.getRouteKey(), param.getRouteKey() != null, null);
    }
}
