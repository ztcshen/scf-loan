package com.scf.loan.job.handler;

import com.xxl.job.core.biz.model.ReturnT;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AbstractJobHandler 单元测试
 * 
 * @author scf-loan
 */
@ExtendWith(MockitoExtension.class)
class AbstractJobHandlerTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Timer timer;

    @Mock
    private Timer.Sample sample;

    private TestJobHandler testJobHandler;

    /**
     * 测试用的具体处理器实现
     */
    private static class TestJobHandler extends AbstractJobHandler {
        private boolean shouldSucceed = true;
        private boolean shouldThrowException = false;
        private ReturnT<String> customResult = null;

        public void setShouldSucceed(boolean shouldSucceed) {
            this.shouldSucceed = shouldSucceed;
        }

        public void setShouldThrowException(boolean shouldThrowException) {
            this.shouldThrowException = shouldThrowException;
        }

        public void setCustomResult(ReturnT<String> customResult) {
            this.customResult = customResult;
        }

        @Override
        protected ReturnT<String> doExecute(String param) throws Exception {
            if (shouldThrowException) {
                throw new RuntimeException("测试异常");
            }
            
            if (customResult != null) {
                return customResult;
            }
            
            return shouldSucceed ? ReturnT.SUCCESS : ReturnT.FAIL;
        }
    }

    @BeforeEach
    void setUp() {
        testJobHandler = new TestJobHandler();
        // 使用ReflectionTestUtils注入mock的meterRegistry
        ReflectionTestUtils.setField(testJobHandler, "meterRegistry", meterRegistry);
        
        // 清理MDC上下文
        MDC.clear();
    }

    @Test
    void testExecuteInternal_Success() {
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(timer);
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试
        ReturnT<String> result = testJobHandler.executeInternal("test-param");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        assertEquals("success", result.getMsg());
        
        // 验证监控指标被记录
        verify(meterRegistry).counter(eq("xxl.job.start.total"), any());
        verify(meterRegistry).counter(eq("xxl.job.success.total"), any());
    }

    @Test
    void testExecuteInternal_Failure() {
        // 设置测试条件
        testJobHandler.setShouldSucceed(false);
        
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(timer);
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试
        ReturnT<String> result = testJobHandler.executeInternal("test-param");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.FAIL_CODE, result.getCode());
        assertEquals("fail", result.getMsg());
        
        // 验证监控指标被记录
        verify(meterRegistry).counter(eq("xxl.job.start.total"), any());
        verify(meterRegistry).counter(eq("xxl.job.failure.total"), any());
    }

    @Test
    void testExecuteInternal_Exception() {
        // 设置测试条件
        testJobHandler.setShouldThrowException(true);
        
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(timer);
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试
        ReturnT<String> result = testJobHandler.executeInternal("test-param");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.FAIL_CODE, result.getCode());
        assertEquals("fail", result.getMsg());
        
        // 验证监控指标被记录
        verify(meterRegistry).counter(eq("xxl.job.start.total"), any());
        verify(meterRegistry).counter(eq("xxl.job.error.total"), any());
    }

    @Test
    void testExecuteInternal_CustomResult() {
        // 设置自定义返回结果
        ReturnT<String> customResult = new ReturnT<>(500, "自定义错误");
        testJobHandler.setCustomResult(customResult);
        
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(timer);
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试
        ReturnT<String> result = testJobHandler.executeInternal("test-param");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("自定义错误", result.getMsg());
        
        // 验证监控指标被记录
        verify(meterRegistry).counter(eq("xxl.job.start.total"), any());
        verify(meterRegistry).counter(eq("xxl.job.failure.total"), any());
    }

    @Test
    void testExecuteInternal_WithJobMethod() {
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(timer);
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试
        ReturnT<String> result = testJobHandler.executeInternal("test-param", "testMethod");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        
        // 验证监控指标被记录，jobName应该包含方法名
        verify(meterRegistry).counter(eq("xxl.job.start.total"), any());
        verify(meterRegistry).counter(eq("xxl.job.success.total"), any());
    }

    @Test
    void testRecordBusinessMetric() {
        // 准备mock对象
        when(meterRegistry.gauge(anyString(), any(), anyDouble())).thenReturn(1.0);
        
        // 执行测试
        testJobHandler.recordBusinessMetric("test.metric", 100.0, "tag1", "value1");
        
        // 验证结果
        verify(meterRegistry).gauge(eq("test.metric"), any(), eq(100.0));
    }

    @Test
    void testIncrementBusinessCounter() {
        // 准备mock对象
        io.micrometer.core.instrument.Counter counter = mock(io.micrometer.core.instrument.Counter.class);
        when(meterRegistry.counter(anyString(), any())).thenReturn(counter);
        
        // 执行测试
        testJobHandler.incrementBusinessCounter("test.counter", "tag1", "value1");
        
        // 验证结果
        verify(meterRegistry).counter(eq("test.counter"), any());
        verify(counter).increment();
    }

    @Test
    void testRecordBusinessDuration() {
        // 准备mock对象
        Timer timer = mock(Timer.class);
        when(meterRegistry.timer(anyString(), any())).thenReturn(timer);
        
        // 执行测试
        testJobHandler.recordBusinessDuration("test.duration", 1000L, "tag1", "value1");
        
        // 验证结果
        verify(meterRegistry).timer(eq("test.duration"), any());
        verify(timer).record(eq(1000L), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    void testGetMeterRegistry() {
        // 执行测试
        MeterRegistry registry = testJobHandler.getMeterRegistry();
        
        // 验证结果
        assertNotNull(registry);
        assertEquals(meterRegistry, registry);
    }

    @Test
    void testGetJobName() {
        // 执行测试
        String jobName = testJobHandler.getJobName();
        
        // 验证结果
        assertNotNull(jobName);
        assertEquals("TestJobHandler", jobName);
    }

    @Test
    void testDoExecute_UnsupportedOperation() {
        // 创建新的处理器，不重写doExecute方法
        AbstractJobHandler handler = new AbstractJobHandler() {};
        ReflectionTestUtils.setField(handler, "meterRegistry", meterRegistry);
        
        // 执行测试并验证异常
        assertThrows(UnsupportedOperationException.class, () -> {
            handler.executeInternal("test-param");
        });
    }

    @Test
    void testDoExecute_WithJobMethod_UnsupportedOperation() {
        // 创建新的处理器，不重写doExecute方法
        AbstractJobHandler handler = new AbstractJobHandler() {};
        ReflectionTestUtils.setField(handler, "meterRegistry", meterRegistry);
        
        // 执行测试并验证异常
        assertThrows(UnsupportedOperationException.class, () -> {
            handler.executeInternal("test-param", "testMethod");
        });
    }

    @Test
    void testMDCContextCleanup() {
        // 准备mock对象
        when(meterRegistry.timer(anyString(), any())).thenReturn(timer);
        when(meterRegistry.counter(anyString(), any())).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
        
        // 执行测试前设置MDC
        MDC.put("testKey", "testValue");
        
        // 执行测试
        testJobHandler.executeInternal("test-param");
        
        // 验证MDC被清理
        assertNull(MDC.get("traceId"));
        assertNull(MDC.get("jobName"));
        assertNull(MDC.get("testKey"));
    }
}