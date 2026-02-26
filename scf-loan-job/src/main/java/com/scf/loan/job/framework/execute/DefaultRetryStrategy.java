package com.scf.loan.job.framework.execute;

import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class DefaultRetryStrategy implements RetryStrategy {
    @Override
    public ReturnT<String> executeWithRetry(ScfCoreCommandParam<?> param, Supplier<ReturnT<String>> action) {
        int retryTimes = param != null && param.getRetryTimes() != null ? param.getRetryTimes() : 0;
        ReturnT<String> result = null;
        for (int i = 0; i <= retryTimes; i++) {
            result = action.get();
            if (result != null && result.getCode() == ReturnT.SUCCESS_CODE) {
                return result;
            }
        }
        return result != null ? result : ReturnT.FAIL;
    }
}
