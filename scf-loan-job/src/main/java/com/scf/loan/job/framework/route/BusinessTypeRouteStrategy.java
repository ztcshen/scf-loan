package com.scf.loan.job.framework.route;

import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BusinessTypeRouteStrategy implements RouteStrategy {
    @Override
    public boolean supports(ScfCoreCommandParam<?> param) {
        return extract(param, "businessType") != null;
    }

    @Override
    public String route(ScfCoreCommandParam<?> param) {
        return extract(param, "businessType");
    }

    @Override
    public int getOrder() {
        return 20;
    }

    private String extract(ScfCoreCommandParam<?> param, String key) {
        if (param == null) {
            return null;
        }
        if (param.getBusinessParam() instanceof Map) {
            Object value = ((Map<?, ?>) param.getBusinessParam()).get(key);
            if (value != null) {
                return value.toString();
            }
        }
        if (param.getExt() != null) {
            Object value = param.getExt().get(key);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
}
