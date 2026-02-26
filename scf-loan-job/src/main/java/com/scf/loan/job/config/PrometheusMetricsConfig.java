package com.scf.loan.job.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Prometheus监控配置类
 * 
 * @author scf-loan
 */
@Configuration
public class PrometheusMetricsConfig {

    /**
     * 自定义MeterRegistry配置
     * 
     * @return MeterRegistry自定义配置
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
                .commonTags("application", "scf-loan-job")
                .commonTags("module", "job-scheduler")
                .meterFilter(MeterFilter.deny(id -> {
                    String name = id.getName();
                    // 过滤掉一些不必要的指标
                    return name.startsWith("jvm.gc.pause") 
                            || name.startsWith("process.")
                            || name.startsWith("system.");
                }));
    }

    /**
     * 任务监控指标配置
     * 
     * @return MeterRegistryCustomizer
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> jobMetricsCustomizer() {
        return registry -> {
            // 配置XXL-JOB相关指标的通用标签
            registry.config().commonTags("metric_type", "xxl_job");
        };
    }
}