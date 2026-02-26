package com.scf.loan.job.framework.dispatcher;

import com.scf.loan.job.framework.command.CommandService;
import com.scf.loan.job.framework.command.ScfCoreCommand;
import com.scf.loan.job.framework.execute.ExecuteService;
import com.scf.loan.job.framework.model.RouteResult;
import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.scf.loan.job.framework.monitor.MonitorService;
import com.scf.loan.job.framework.route.RouteService;
import com.scf.loan.job.framework.trace.TraceContext;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UnifiedJobDispatcher {
    private final RouteService routeService;
    private final CommandService commandService;
    private final ExecuteService executeService;
    private final MonitorService monitorService;

    @Autowired
    public UnifiedJobDispatcher(RouteService routeService,
                                CommandService commandService,
                                ExecuteService executeService,
                                MonitorService monitorService) {
        this.routeService = routeService;
        this.commandService = commandService;
        this.executeService = executeService;
        this.monitorService = monitorService;
    }

    public ReturnT<String> dispatch(ScfCoreCommandParam<?> param) {
        if (param == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "param is null");
        }
        if (param.getCommandType() == null || param.getCommandType().isBlank()) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "commandType is blank");
        }
        if (param.getTraceLogId() == null || param.getTraceLogId().isBlank()) {
            param.setTraceLogId(TraceContext.initRoot("SC", param.getCommandType()));
        }
        MDC.put("traceId", param.getTraceLogId());

        monitorService.recordExecute(param.getExecuteType() == null ? "sync" : param.getExecuteType());

        long routeStart = System.currentTimeMillis();
        RouteResult routeResult = routeService.route(param);
        monitorService.recordRoute(routeResult, System.currentTimeMillis() - routeStart);

        ScfCoreCommand<?> command = commandService.getCommand(param.getCommandType());
        if (command == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "command not found: " + param.getCommandType());
        }

        long commandStart = System.currentTimeMillis();
        try {
            ReturnT<String> result = executeService.execute(param, command);
            monitorService.recordCommand(param.getCommandType(), result != null && result.getCode() == ReturnT.SUCCESS_CODE,
                    System.currentTimeMillis() - commandStart);
            return result;
        } catch (Exception ex) {
            monitorService.recordCommand(param.getCommandType(), false, System.currentTimeMillis() - commandStart);
            monitorService.recordDispatcherError(ex.getClass().getSimpleName());
            log.error("dispatch failed, commandType={}", param.getCommandType(), ex);
            return new ReturnT<>(ReturnT.FAIL_CODE, ex.getMessage());
        }
    }
}
