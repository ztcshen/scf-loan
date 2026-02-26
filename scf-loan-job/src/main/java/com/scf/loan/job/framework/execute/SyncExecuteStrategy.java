package com.scf.loan.job.framework.execute;

import com.scf.loan.job.framework.command.ScfCoreCommand;
import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.stereotype.Component;

@Component
public class SyncExecuteStrategy implements ExecuteStrategy {
    @Override
    public String getExecuteType() {
        return "sync";
    }

    @Override
    public ReturnT<String> execute(ScfCoreCommandParam<?> param, ScfCoreCommand<?> command, RetryStrategy retryStrategy) {
        return retryStrategy.executeWithRetry(param, () -> command.execute((ScfCoreCommandParam<Object>) param));
    }
}
