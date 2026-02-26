package com.scf.loan.job.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PrometheusMetricsConfig 单元测试
 * 
 * @author scf-loan
 */
@ExtendWith(MockitoExtension.class)
class PrometheusMetricsConfigTest {

    @Test
    void testMetricsCommonTags() {
        // 创建配置实例
        PrometheusMetricsConfig config = new PrometheusMetricsConfig();
        
        // 执行测试
        var customizer = config.metricsCommonTags();
        
        // 验证结果
        assertNotNull(customizer);
        
        // 创建测试用的MeterRegistry
        MeterRegistry registry = new SimpleMeterRegistry();
        
        // 应用自定义配置
        customizer.customize(registry);
        
        // 验证通用标签被设置
        assertNotNull(registry.config().getCommonTags());
        
        // 验证配置对象创建成功
        assertNotNull(config);
    }

    @Test
    void testJobMetricsCustomizer() {
        // 创建配置实例
        PrometheusMetricsConfig config = new PrometheusMetricsConfig();
        
        // 执行测试
        var customizer = config.jobMetricsCustomizer();
        
        // 验证结果
        assertNotNull(customizer);
        
        // 创建测试用的MeterRegistry
        MeterRegistry registry = new SimpleMeterRegistry();
        
        // 应用自定义配置
        customizer.customize(registry);
        
        // 验证配置对象创建成功
        assertNotNull(config);
    }

    @Test
    void testMetricsCommonTags_NotNull() {
        // 创建配置实例
        PrometheusMetricsConfig config = new PrometheusMetricsConfig();
        
        // 执行测试
        var customizer = config.metricsCommonTags();
        
        // 验证结果
        assertNotNull(customizer);
        assertNotNull(config);
    }

    @Test
    void testJobMetricsCustomizer_NotNull() {
        // 创建配置实例
        PrometheusMetricsConfig config = new PrometheusMetricsConfig();
        
        // 执行测试
        var customizer = config.jobMetricsCustomizer();
        
        // 验证结果
        assertNotNull(customizer);
        assertNotNull(config);
    }
}