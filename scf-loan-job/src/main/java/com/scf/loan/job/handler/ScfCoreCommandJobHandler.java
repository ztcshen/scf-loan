package com.scf.loan.job.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.loan.job.framework.dispatcher.UnifiedJobDispatcher;
import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ScfCoreCommandJobHandler extends AbstractJobHandler {

    @Autowired
    private UnifiedJobDispatcher dispatcher;

    @Autowired
    private ObjectMapper objectMapper;

    @XxlJob("scfCoreCommandJob")
    public ReturnT<String> executeScfCoreCommand(String param) throws Exception {
        return executeInternal(param, "scfCoreCommand");
    }

    @Override
    protected ReturnT<String> doExecute(String param, String jobMethod) {
        if (param == null || param.isBlank()) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "param is blank");
        }
        try {
            ScfCoreCommandParam<Map<String, Object>> commandParam = objectMapper.readValue(
                    param, new TypeReference<ScfCoreCommandParam<Map<String, Object>>>() {
                    });
            return dispatcher.dispatch(commandParam);
        } catch (Exception ex) {
            log.error("parse command param failed", ex);
            return new ReturnT<>(ReturnT.FAIL_CODE, ex.getMessage());
        }
    }
}
