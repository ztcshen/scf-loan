package com.scf.loan.job.framework.execute;

import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.function.Supplier;

public interface RetryStrategy {
    ReturnT<String> executeWithRetry(ScfCoreCommandParam<?> param, Supplier<ReturnT<String>> action);
}
