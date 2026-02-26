package com.scf.loan.job.framework.command;

import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.xxl.job.core.biz.model.ReturnT;

public interface ScfCoreCommand<T> {
    String getCommandType();

    ReturnT<String> execute(ScfCoreCommandParam<T> param);

    default boolean supports(ScfCoreCommandParam<?> param) {
        return true;
    }
}
