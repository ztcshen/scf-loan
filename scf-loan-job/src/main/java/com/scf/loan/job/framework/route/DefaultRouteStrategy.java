package com.scf.loan.job.framework.route;

import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import org.springframework.stereotype.Component;

@Component
public class DefaultRouteStrategy implements RouteStrategy {
    @Override
    public boolean supports(ScfCoreCommandParam<?> param) {
        return true;
    }

    @Override
    public String route(ScfCoreCommandParam<?> param) {
        return param == null ? null : param.getRouteKey();
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
