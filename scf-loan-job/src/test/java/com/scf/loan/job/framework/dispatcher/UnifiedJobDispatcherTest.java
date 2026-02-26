package com.scf.loan.job.framework.dispatcher;

import com.scf.loan.job.framework.command.CommandRegistry;
import com.scf.loan.job.framework.command.CommandService;
import com.scf.loan.job.framework.command.ScfCoreCommand;
import com.scf.loan.job.framework.execute.DefaultRetryStrategy;
import com.scf.loan.job.framework.execute.ExecuteService;
import com.scf.loan.job.framework.execute.SyncExecuteStrategy;
import com.scf.loan.job.framework.model.ScfCoreCommandParam;
import com.scf.loan.job.framework.monitor.MonitorService;
import com.scf.loan.job.framework.route.RouteService;
import com.scf.loan.job.framework.route.RouteStrategy;
import com.xxl.job.core.biz.model.ReturnT;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnifiedJobDispatcherTest {

    @Test
    void dispatchSuccess() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        RouteStrategy routeStrategy = new RouteStrategy() {
            @Override
            public boolean supports(ScfCoreCommandParam<?> param) {
                return true;
            }

            @Override
            public String route(ScfCoreCommandParam<?> param) {
                return "routeA";
            }

            @Override
            public int getOrder() {
                return 0;
            }
        };
        RouteService routeService = new RouteService(List.of(routeStrategy));

        ScfCoreCommand<String> command = new ScfCoreCommand<>() {
            @Override
            public String getCommandType() {
                return "TEST";
            }

            @Override
            public ReturnT<String> execute(ScfCoreCommandParam<String> param) {
                return ReturnT.SUCCESS;
            }
        };
        CommandRegistry commandRegistry = new CommandRegistry();
        CommandService commandService = new CommandService(commandRegistry, List.of(command));

        ExecuteService executeService = new ExecuteService(List.of(new SyncExecuteStrategy()), new DefaultRetryStrategy());
        MonitorService monitorService = new MonitorService(meterRegistry);
        UnifiedJobDispatcher dispatcher = new UnifiedJobDispatcher(routeService, commandService, executeService, monitorService);

        ScfCoreCommandParam<String> param = new ScfCoreCommandParam<>();
        param.setCommandType("TEST");
        param.setExecuteType("sync");

        ReturnT<String> result = dispatcher.dispatch(param);

        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        assertEquals("routeA", param.getRouteKey());
    }

    @Test
    void dispatchMissingCommand() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        RouteService routeService = new RouteService(List.of());
        CommandRegistry commandRegistry = new CommandRegistry();
        CommandService commandService = new CommandService(commandRegistry, List.of());
        ExecuteService executeService = new ExecuteService(List.of(new SyncExecuteStrategy()), new DefaultRetryStrategy());
        MonitorService monitorService = new MonitorService(meterRegistry);
        UnifiedJobDispatcher dispatcher = new UnifiedJobDispatcher(routeService, commandService, executeService, monitorService);

        ScfCoreCommandParam<String> param = new ScfCoreCommandParam<>();
        param.setCommandType("MISSING");
        param.setExecuteType("sync");

        ReturnT<String> result = dispatcher.dispatch(param);

        assertNotNull(result);
        assertEquals(ReturnT.FAIL_CODE, result.getCode());
        assertTrue(result.getMsg().contains("command"));
    }
}
