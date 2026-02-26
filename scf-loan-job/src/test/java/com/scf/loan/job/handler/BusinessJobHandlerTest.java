package com.scf.loan.job.handler;

import com.scf.loan.job.service.DemoJobService;
import com.xxl.job.core.biz.model.ReturnT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * BusinessJobHandler 单元测试
 * 
 * @author scf-loan
 */
@ExtendWith(MockitoExtension.class)
class BusinessJobHandlerTest {

    @Mock
    private DemoJobService demoJobService;

    @InjectMocks
    private BusinessJobHandler businessJobHandler;

    @BeforeEach
    void setUp() {
        // 不需要额外的设置，Mockito会自动注入
    }

    @Test
    void testExecuteDemoBusiness_Success() throws Exception {
        // 准备测试数据
        String param = "test-param";
        when(demoJobService.executeDemoBusiness(param)).thenReturn(true);
        
        // 执行测试
        ReturnT<String> result = businessJobHandler.executeDemoBusiness(param);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        assertEquals("success", result.getMsg());
        
        // 验证服务调用
        verify(demoJobService).executeDemoBusiness(param);
    }

    @Test
    void testExecuteDemoBusiness_Failure() throws Exception {
        // 准备测试数据
        String param = "test-param";
        when(demoJobService.executeDemoBusiness(param)).thenReturn(false);
        
        // 执行测试
        ReturnT<String> result = businessJobHandler.executeDemoBusiness(param);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.FAIL_CODE, result.getCode());
        assertEquals("fail", result.getMsg());
        
        // 验证服务调用
        verify(demoJobService).executeDemoBusiness(param);
    }

    @Test
    void testExecuteDemoBusiness_ServiceException() throws Exception {
        // 准备测试数据
        String param = "test-param";
        when(demoJobService.executeDemoBusiness(param)).thenThrow(new RuntimeException("服务异常"));
        
        // 执行测试
        ReturnT<String> result = businessJobHandler.executeDemoBusiness(param);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.FAIL_CODE, result.getCode());
        assertEquals("fail", result.getMsg());
        
        // 验证服务调用
        verify(demoJobService).executeDemoBusiness(param);
    }

    @Test
    void testExecuteDataSync_WithValidParam() throws Exception {
        // 准备测试数据
        String param = "123";
        String expectedData = "DemoData_123_1234567890";
        when(demoJobService.getDemoData(123L)).thenReturn(expectedData);
        
        // 执行测试
        ReturnT<String> result = businessJobHandler.executeDataSync(param);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        assertEquals("success", result.getMsg());
        
        // 验证服务调用
        verify(demoJobService).getDemoData(123L);
    }

    @Test
    void testExecuteDataSync_WithInvalidParam() throws Exception {
        // 准备测试数据
        String param = "invalid-number";
        String expectedData = "DemoData_1_1234567890";
        when(demoJobService.getDemoData(1L)).thenReturn(expectedData);
        
        // 执行测试
        ReturnT<String> result = businessJobHandler.executeDataSync(param);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        assertEquals("success", result.getMsg());
        
        // 验证服务调用（应该使用默认值1L）
        verify(demoJobService).getDemoData(1L);
    }

    @Test
    void testExecuteDataSync_WithEmptyParam() throws Exception {
        // 准备测试数据
        String param = "";
        String expectedData = "DemoData_1_1234567890";
        when(demoJobService.getDemoData(1L)).thenReturn(expectedData);
        
        // 执行测试
        ReturnT<String> result = businessJobHandler.executeDataSync(param);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        assertEquals("success", result.getMsg());
        
        // 验证服务调用（应该使用默认值1L）
        verify(demoJobService).getDemoData(1L);
    }

    @Test
    void testExecuteDataSync_WithNullParam() throws Exception {
        // 准备测试数据
        String param = null;
        String expectedData = "DemoData_1_1234567890";
        when(demoJobService.getDemoData(1L)).thenReturn(expectedData);
        
        // 执行测试
        ReturnT<String> result = businessJobHandler.executeDataSync(param);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.SUCCESS_CODE, result.getCode());
        assertEquals("success", result.getMsg());
        
        // 验证服务调用（应该使用默认值1L）
        verify(demoJobService).getDemoData(1L);
    }

    @Test
    void testExecuteDataSync_ServiceException() throws Exception {
        // 准备测试数据
        String param = "123";
        when(demoJobService.getDemoData(123L)).thenThrow(new RuntimeException("服务异常"));
        
        // 执行测试
        ReturnT<String> result = businessJobHandler.executeDataSync(param);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(ReturnT.FAIL_CODE, result.getCode());
        assertEquals("fail", result.getMsg());
        
        // 验证服务调用
        verify(demoJobService).getDemoData(123L);
    }
}