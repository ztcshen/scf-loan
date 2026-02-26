package com.scf.loan.job.framework.execute;

import com.scf.loan.job.framework.command.ScfCoreCommand;
import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.xxl.job.core.biz.model.ReturnT;

public interface ExecuteStrategy {
    String getExecuteType();

    ReturnT<String> execute(ScfCoreCommandParam<?> param, ScfCoreCommand<?> command, RetryStrategy retryStrategy);
}
