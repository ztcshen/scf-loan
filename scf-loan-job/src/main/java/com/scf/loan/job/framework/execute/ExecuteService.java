package com.scf.loan.job.framework.execute;

import com.scf.loan.job.framework.command.ScfCoreCommand;
import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExecuteService {
    private final Map<String, ExecuteStrategy> strategies = new HashMap<>();
    private final RetryStrategy retryStrategy;

    @Autowired
    public ExecuteService(List<ExecuteStrategy> strategies, RetryStrategy retryStrategy) {
        if (strategies != null) {
            for (ExecuteStrategy strategy : strategies) {
                this.strategies.put(strategy.getExecuteType(), strategy);
            }
        }
        this.retryStrategy = retryStrategy;
    }

    public ReturnT<String> execute(ScfCoreCommandParam<?> param, ScfCoreCommand<?> command) {
        String executeType = param != null && param.getExecuteType() != null ? param.getExecuteType() : "sync";
        ExecuteStrategy strategy = strategies.get(executeType);
        if (strategy == null) {
            strategy = strategies.get("sync");
        }
        if (strategy == null) {
            return ReturnT.FAIL;
        }
        return strategy.execute(param, command, retryStrategy);
    }
}
