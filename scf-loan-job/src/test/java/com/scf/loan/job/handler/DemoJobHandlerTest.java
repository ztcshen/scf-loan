package com.scf.loan.job.handler;

import com.xxl.job.core.biz.model.ReturnT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import io.micrometer.core.instrument.MeterRegistry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * DemoJobHandler 单元测试
 * 
 * @author scf-loan
 */
@ExtendWith(MockitoExtension.class)
class DemoJobHandlerTest {

    @Mock
    private MeterRegistry meterRegistry;

    @InjectMocks
    private DemoJobHandler demoJobHandler;

    @BeforeEach
    void setUp() {
        // 不需要额外的设置，Mockito会自动注入
    }

    @Test
    void testExecute_Success() throws Exception {
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Timer.class));
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试
        ReturnT<String> result = demoJobHandler.execute("test-param");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        assertEquals("success", result.getMsg());
    }

    @Test
    void testExecute_WithException() throws Exception {
        // 创建一个会抛出异常的测试处理器
        DemoJobHandler exceptionHandler = new DemoJobHandler() {
            @Override
            protected ReturnT<String> doExecute(String param) throws Exception {
                throw new RuntimeException("测试异常");
            }
        };
        ReflectionTestUtils.setField(exceptionHandler, "meterRegistry", meterRegistry);
        
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Timer.class));
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试
        ReturnT<String> result = exceptionHandler.execute("test-param");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.FAIL_CODE, result.getCode());
        assertEquals("fail", result.getMsg());
    }

    @Test
    void testTestScheduledExecute_Success() throws Exception {
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Timer.class));
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试
        ReturnT<String> result = demoJobHandler.testScheduledExecute("test-param");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        assertEquals("success", result.getMsg());
    }

    @Test
    void testTestScheduledExecute_WithException() throws Exception {
        // 创建一个会抛出异常的测试处理器
        DemoJobHandler exceptionHandler = new DemoJobHandler() {
            @Override
            protected ReturnT<String> doExecute(String param) throws Exception {
                throw new RuntimeException("测试异常");
            }
        };
        ReflectionTestUtils.setField(exceptionHandler, "meterRegistry", meterRegistry);
        
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Timer.class));
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试
        ReturnT<String> result = exceptionHandler.testScheduledExecute("test-param");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.FAIL_CODE, result.getCode());
        assertEquals("fail", result.getMsg());
    }

    @Test
    void testDoExecute_BusinessMetrics() throws Exception {
        // 创建一个自定义的测试处理器来验证业务指标记录
        DemoJobHandler metricsHandler = new DemoJobHandler() {
            @Override
            protected ReturnT<String> doExecute(String param) throws Exception {
                // 记录业务指标
                recordBusinessCounter("demo.task.processed.count");
                recordBusinessMetric("demo.task.processed.data.size", 100);
                return ReturnT.SUCCESS;
            }
        };
        ReflectionTestUtils.setField(metricsHandler, "meterRegistry", meterRegistry);
        
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Timer.class));
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        when(meterRegistry.gauge(anyString(), any(), anyDouble())).thenReturn(100.0);
        
        // 执行测试
        ReturnT<String> result = metricsHandler.execute("test-param");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        
        // 验证业务指标被记录
        verify(meterRegistry).counter(eq("demo.task.processed.count"), any());
        verify(meterRegistry).gauge(eq("demo.task.processed.data.size"), any(), eq(100.0));
    }
}