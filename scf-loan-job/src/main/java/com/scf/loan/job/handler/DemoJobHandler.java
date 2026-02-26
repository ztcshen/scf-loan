package com.scf.loan.job.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Demo XXL-JOB任务处理器
 * 使用抽象基类提供统一的监控和日志功能
 * 
 * @author scf-loan
 */
@Slf4j
@Component
public class DemoJobHandler extends AbstractJobHandler {

    /**
     * Demo任务示例
     * 
     * @param param 任务参数
     * @return 执行结果
     * @throws Exception 异常
     */
    @XxlJob("demoJob")
    public ReturnT<String> execute(String param) throws Exception {
        return executeInternal(param);
    }

    /**
     * 定时测试任务
     * 
     * @param param 任务参数
     * @return 执行结果
     * @throws Exception 异常
     */
    @XxlJob("testScheduledJob")
    public ReturnT<String> testScheduledExecute(String param) throws Exception {
        return executeInternal(param);
    }

    @Override
    protected ReturnT<String> doExecute(String param) throws Exception {
        log.info("开始执行Demo任务，参数：{}", param);
        
        try {
            // 模拟业务处理
            log.info("Demo任务正在处理中...");
            
            // 这里可以添加具体的业务逻辑
            // 例如：调用服务、处理数据等
            
            // 模拟处理时间
            Thread.sleep(1000);
            
            // 记录业务指标
            recordBusinessCounter("demo.task.processed.count");
            recordBusinessMetric("demo.task.processed.data.size", 100);
            
            log.info("Demo任务执行成功");
            return ReturnT.SUCCESS;
            
        } catch (Exception e) {
            log.error("Demo任务执行失败", e);
            return ReturnT.FAIL;
        }
    }
}