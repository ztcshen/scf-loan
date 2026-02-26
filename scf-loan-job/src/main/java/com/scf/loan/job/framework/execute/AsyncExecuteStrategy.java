package com.scf.loan.job.framework.execute;

import com.scf.loan.job.framework.command.ScfCoreCommand;
import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AsyncExecuteStrategy implements ExecuteStrategy {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public String getExecuteType() {
        return "async";
    }

    @Override
    public ReturnT<String> execute(ScfCoreCommandParam<?> param, ScfCoreCommand<?> command, RetryStrategy retryStrategy) {
        executorService.submit(() -> retryStrategy.executeWithRetry(param, () -> command.execute((ScfCoreCommandParam<Object>) param)));
        return new ReturnT<>(ReturnT.SUCCESS_CODE, "async submitted");
    }
}
