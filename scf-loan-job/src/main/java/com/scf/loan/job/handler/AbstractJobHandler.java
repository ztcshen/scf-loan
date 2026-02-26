package com.scf.loan.job.handler;

import com.xxl.job.core.biz.model.ReturnT;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * XXL-JOB任务处理器抽象基类
 * 提供统一的监控、日志、性能统计等功能
 * 
 * @author scf-loan
 */
@Slf4j
public abstract class AbstractJobHandler {

    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * 任务执行模板方法
     * 
     * @param param 任务参数
     * @return 执行结果
     */
    protected ReturnT<String> executeInternal(String param) {
        String jobName = this.getClass().getSimpleName();
        String traceId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        
        // 设置MDC上下文
        MDC.put("traceId", traceId);
        MDC.put("jobName", jobName);
        
        log.info("[XXL-JOB] 开始执行任务: {}, 参数: {}", jobName, param);
        
        Timer.Sample sample = Timer.start(meterRegistry);
        ReturnT<String> result;
        
        try {
            // 记录任务开始指标
            recordJobStart(jobName);
            
            // 执行具体的业务逻辑
            result = doExecute(param);
            
            // 记录任务完成指标
            long duration = System.currentTimeMillis() - startTime;
            sample.stop(Timer.builder("xxl.job.duration")
                    .tag("job_name", jobName)
                    .tag("status", result.getCode() == ReturnT.SUCCESS_CODE ? "success" : "failed")
                    .register(meterRegistry));
            
            if (result.getCode() == ReturnT.SUCCESS_CODE) {
                log.info("[XXL-JOB] 任务执行成功: {}, 耗时: {}ms", jobName, duration);
                recordJobSuccess(jobName, duration);
            } else {
                log.warn("[XXL-JOB] 任务执行失败: {}, 错误码: {}, 错误信息: {}, 耗时: {}ms", 
                        jobName, result.getCode(), result.getMsg(), duration);
                recordJobFailure(jobName, result.getMsg(), duration);
            }
            
            return result;
            
        } catch (Exception e) {
            // 记录异常指标
            long duration = System.currentTimeMillis() - startTime;
            sample.stop(Timer.builder("xxl.job.duration")
                    .tag("job_name", jobName)
                    .tag("status", "error")
                    .register(meterRegistry));
            
            log.error("[XXL-JOB] 任务执行异常: {}, 耗时: {}ms", jobName, duration, e);
            recordJobError(jobName, e, duration);
            
            return ReturnT.FAIL;
            
        } finally {
            // 清理MDC上下文
            MDC.clear();
        }
    }

    /**
     * 任务执行模板方法（带任务名称参数）
     * 用于支持同一个类中的多个任务方法
     * 
     * @param param 任务参数
     * @param jobMethod 任务方法名称
     * @return 执行结果
     */
    protected ReturnT<String> executeInternal(String param, String jobMethod) {
        String jobName = this.getClass().getSimpleName() + "." + jobMethod;
        String traceId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        
        // 设置MDC上下文
        MDC.put("traceId", traceId);
        MDC.put("jobName", jobName);
        
        log.info("[XXL-JOB] 开始执行任务: {}, 参数: {}", jobName, param);
        
        Timer.Sample sample = Timer.start(meterRegistry);
        ReturnT<String> result;
        
        try {
            // 记录任务开始指标
            recordJobStart(jobName);
            
            // 执行具体的业务逻辑
            result = doExecute(param, jobMethod);
            
            // 记录任务完成指标
            long duration = System.currentTimeMillis() - startTime;
            sample.stop(Timer.builder("xxl.job.duration")
                    .tag("job_name", jobName)
                    .tag("status", result.getCode() == ReturnT.SUCCESS_CODE ? "success" : "failed")
                    .register(meterRegistry));
            
            if (result.getCode() == ReturnT.SUCCESS_CODE) {
                log.info("[XXL-JOB] 任务执行成功: {}, 耗时: {}ms", jobName, duration);
                recordJobSuccess(jobName, duration);
            } else {
                log.warn("[XXL-JOB] 任务执行失败: {}, 错误码: {}, 错误信息: {}, 耗时: {}ms", 
                        jobName, result.getCode(), result.getMsg(), duration);
                recordJobFailure(jobName, result.getMsg(), duration);
            }
            
            return result;
            
        } catch (Exception e) {
            // 记录异常指标
            long duration = System.currentTimeMillis() - startTime;
            sample.stop(Timer.builder("xxl.job.duration")
                    .tag("job_name", jobName)
                    .tag("status", "error")
                    .register(meterRegistry));
            
            log.error("[XXL-JOB] 任务执行异常: {}, 耗时: {}ms", jobName, duration, e);
            recordJobError(jobName, e, duration);
            
            return ReturnT.FAIL;
            
        } finally {
            // 清理MDC上下文
            MDC.clear();
        }
    }

    /**
     * 具体的任务执行逻辑，由子类实现
     * 单任务处理器使用此方法
     * 
     * @param param 任务参数
     * @return 执行结果
     * @throws Exception 异常
     */
    protected ReturnT<String> doExecute(String param) throws Exception {
        throw new UnsupportedOperationException("单任务处理器请重写doExecute(String param)方法");
    }

    /**
     * 具体的任务执行逻辑，由子类实现
     * 多任务处理器使用此方法
     * 
     * @param param 任务参数
     * @param jobMethod 任务方法名称
     * @return 执行结果
     * @throws Exception 异常
     */
    protected ReturnT<String> doExecute(String param, String jobMethod) throws Exception {
        throw new UnsupportedOperationException("多任务处理器请重写doExecute(String param, String jobMethod)方法");
    }

    /**
     * 获取任务名称，默认使用类名
     * 
     * @return 任务名称
     */
    protected String getJobName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 记录任务开始指标
     * 
     * @param jobName 任务名称
     */
    private void recordJobStart(String jobName) {
        meterRegistry.counter("xxl.job.start.total", "job_name", jobName).increment();
    }

    /**
     * 记录任务成功指标
     * 
     * @param jobName 任务名称
     * @param duration 执行耗时（毫秒）
     */
    private void recordJobSuccess(String jobName, long duration) {
        meterRegistry.counter("xxl.job.success.total", "job_name", jobName).increment();
        meterRegistry.gauge("xxl.job.duration.success", duration);
        
        // 记录慢任务
        if (duration > 5000) { // 5秒以上为慢任务
            meterRegistry.counter("xxl.job.slow.total", "job_name", jobName).increment();
            log.warn("[XXL-JOB] 慢任务警告: {}, 耗时: {}ms", jobName, duration);
        }
    }

    /**
     * 记录任务失败指标
     * 
     * @param jobName 任务名称
     * @param errorMsg 错误信息
     * @param duration 执行耗时（毫秒）
     */
    private void recordJobFailure(String jobName, String errorMsg, long duration) {
        meterRegistry.counter("xxl.job.failure.total", 
                "job_name", jobName, 
                "error_msg", errorMsg != null ? errorMsg : "unknown").increment();
        meterRegistry.gauge("xxl.job.duration.failure", duration);
    }

    /**
     * 记录任务异常指标
     * 
     * @param jobName 任务名称
     * @param exception 异常
     * @param duration 执行耗时（毫秒）
     */
    private void recordJobError(String jobName, Exception exception, long duration) {
        String errorType = exception.getClass().getSimpleName();
        meterRegistry.counter("xxl.job.error.total", 
                "job_name", jobName, 
                "error_type", errorType).increment();
        meterRegistry.gauge("xxl.job.duration.error", duration);
    }

    /**
     * 记录自定义业务指标
     * 
     * @param metricName 指标名称
     * @param value 指标值
     * @param tags 标签
     */
    protected void recordBusinessMetric(String metricName, double value, String... tags) {
        meterRegistry.gauge(metricName, tags, value);
    }

    /**
     * 记录自定义业务计数器
     * 
     * @param metricName 指标名称
     * @param tags 标签
     */
    protected void incrementBusinessCounter(String metricName, String... tags) {
        meterRegistry.counter(metricName, tags).increment();
    }

    /**
     * 记录业务处理时间
     * 
     * @param metricName 指标名称
     * @param duration 耗时（毫秒）
     * @param tags 标签
     */
    protected void recordBusinessDuration(String metricName, long duration, String... tags) {
        meterRegistry.timer(metricName, tags).record(duration, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取MeterRegistry用于自定义监控
     * 
     * @return MeterRegistry
     */
    protected MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}