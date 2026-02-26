package com.scf.loan.job.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DemoJobService 单元测试
 * 
 * @author scf-loan
 */
@ExtendWith(MockitoExtension.class)
class DemoJobServiceTest {

    @InjectMocks
    private DemoJobService demoJobService;

    @Test
    void testExecuteDemoBusiness_Success() {
        // 执行测试
        boolean result = demoJobService.executeDemoBusiness("test-param");
        
        // 验证结果
        assertTrue(result);
    }

    @Test
    void testGetDemoData_ValidId() {
        // 准备测试数据
        Long id = 123L;
        
        // 执行测试
        String result = demoJobService.getDemoData(id);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.startsWith("DemoData_123_"));
        assertTrue(result.contains(String.valueOf(System.currentTimeMillis())));
    }

    @Test
    void testGetDemoData_NullId() {
        // 执行测试
        String result = demoJobService.getDemoData(null);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.startsWith("DemoData_null_"));
    }

    @Test
    void testGetDemoData_ZeroId() {
        // 准备测试数据
        Long id = 0L;
        
        // 执行测试
        String result = demoJobService.getDemoData(id);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.startsWith("DemoData_0_"));
    }

    @Test
    void testGetDemoData_NegativeId() {
        // 准备测试数据
        Long id = -1L;
        
        // 执行测试
        String result = demoJobService.getDemoData(id);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.startsWith("DemoData_-1_"));
    }
}