package com.scf.loan.job.framework.route;

import com.scf.loan.job.framework.model.ScfCoreCommandParam;

public interface RouteStrategy {
    boolean supports(ScfCoreCommandParam<?> param);

    String route(ScfCoreCommandParam<?> param);

    int getOrder();
}
