package com.scf.loan.job.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 错误处理和重试机制示例
 * 
 * @author scf-loan
 */
@Slf4j
@Component
public class RetryJobHandler {

    /**
     * 带重试机制的任务示例
     * 
     * @param param 任务参数（可以包含重试次数等）
     * @return 执行结果
     * @throws Exception 异常
     */
    @XxlJob("retryableJob")
    public ReturnT<String> executeRetryableJob(String param) throws Exception {
        log.info("开始执行可重试任务，参数：{}", param);
        
        int maxRetries = 3;
        int retryCount = 0;
        Exception lastException = null;
        
        while (retryCount < maxRetries) {
            try {
                log.info("第 {} 次尝试执行任务", retryCount + 1);
                
                // 模拟可能失败的操作
                performRiskyOperation();
                
                log.info("任务执行成功");
                return ReturnT.SUCCESS;
                
            } catch (Exception e) {
                lastException = e;
                retryCount++;
                log.warn("第 {} 次尝试失败：{}", retryCount, e.getMessage());
                
                if (retryCount < maxRetries) {
                    // 等待一段时间后重试
                    Thread.sleep(1000 * retryCount);
                }
            }
        }
        
        log.error("任务在 {} 次尝试后仍然失败", maxRetries, lastException);
        return ReturnT.FAIL;
    }

    /**
     * 模拟可能失败的操作
     * 
     * @throws Exception 模拟的异常
     */
    private void performRiskyOperation() throws Exception {
        // 随机失败，用于演示重试机制
        if (Math.random() < 0.7) {
            throw new RuntimeException("模拟的操作失败");
        }
        
        log.info("风险操作执行成功");
    }

    /**
     * 超时处理任务示例
     * 
     * @param param 任务参数
     * @return 执行结果
     * @throws Exception 异常
     */
    @XxlJob("timeoutHandleJob")
    public ReturnT<String> executeTimeoutHandleJob(String param) throws Exception {
        log.info("开始执行超时处理任务，参数：{}", param);
        
        try {
            // 设置超时时间（毫秒）
            long timeout = 5000;
            long startTime = System.currentTimeMillis();
            
            // 模拟长时间运行的任务
            while (System.currentTimeMillis() - startTime < timeout) {
                // 检查是否应该继续执行
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("任务被中断");
                    return ReturnT.FAIL;
                }
                
                // 模拟工作
                Thread.sleep(100);
                
                // 检查超时
                if (System.currentTimeMillis() - startTime >= timeout) {
                    log.warn("任务执行超时");
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "任务执行超时");
                }
            }
            
            log.info("超时处理任务执行成功");
            return ReturnT.SUCCESS;
            
        } catch (InterruptedException e) {
            log.warn("任务被中断", e);
            Thread.currentThread().interrupt();
            return ReturnT.FAIL;
        } catch (Exception e) {
            log.error("超时处理任务执行失败", e);
            return ReturnT.FAIL;
        }
    }
}