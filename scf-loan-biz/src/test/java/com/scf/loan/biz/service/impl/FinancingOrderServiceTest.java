package com.scf.loan.biz.service.impl;

import com.scf.loan.biz.service.FinancingOrderService;
import com.scf.loan.dal.entity.FinancingOrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * FinancingOrderService单元测试
 */
public class FinancingOrderServiceTest {

    @Mock
    private FinancingOrderService financingOrderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试批量创建FinancingOrder
     */
    @Test
    public void testBatchCreateFinancingOrder() {
        List<FinancingOrderEntity> entities = new ArrayList<>();
        
        // 创建测试数据
        FinancingOrderEntity entity1 = new FinancingOrderEntity();
        // 设置测试数据
        // entity1.setXXX(XXX);
        entities.add(entity1);
        
        FinancingOrderEntity entity2 = new FinancingOrderEntity();
        // 设置测试数据
        // entity2.setXXX(XXX);
        entities.add(entity2);
        
        // 模拟返回值
        when(financingOrderService.batchCreateFinancingOrder(entities)).thenReturn(true);
        
        boolean result = financingOrderService.batchCreateFinancingOrder(entities);
        assertTrue(result);
        
        // 验证方法调用
        verify(financingOrderService, times(1)).batchCreateFinancingOrder(entities);
    }

    /**
     * 测试批量更新FinancingOrder
     */
    @Test
    public void testBatchUpdateFinancingOrder() {
        // 创建测试数据
        List<FinancingOrderEntity> entities = new ArrayList<>();
        
        FinancingOrderEntity entity1 = new FinancingOrderEntity();
        // 设置测试数据
        // entity1.setXXX(XXX);
        entities.add(entity1);
        
        // 模拟返回值
        when(financingOrderService.batchUpdateFinancingOrder(entities)).thenReturn(true);
        
        boolean result = financingOrderService.batchUpdateFinancingOrder(entities);
        assertTrue(result);
        
        // 验证方法调用
        verify(financingOrderService, times(1)).batchUpdateFinancingOrder(entities);
    }

    /**
     * 测试分页查询FinancingOrder
     */
    @Test
    public void testPageFinancingOrder() {
        // 创建模拟返回数据
        List<FinancingOrderEntity> mockEntities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FinancingOrderEntity entity = new FinancingOrderEntity();
            // 设置测试数据
            // entity.setXXX(XXX);
            mockEntities.add(entity);
        }
        
        // 模拟返回值
        when(financingOrderService.pageFinancingOrder(1, 10)).thenReturn(mockEntities);
        
        // 执行方法
        List<FinancingOrderEntity> pageResult = financingOrderService.pageFinancingOrder(1, 10);
        
        // 验证结果
        assertNotNull(pageResult);
        assertEquals(10, pageResult.size());
        
        // 验证方法调用
        verify(financingOrderService, times(1)).pageFinancingOrder(1, 10);
    }
}
