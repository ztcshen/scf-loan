package com.scf.loan.job.handler;

import com.scf.loan.job.service.DemoJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 业务相关的XXL-JOB任务处理器
 * 使用抽象基类提供统一的监控和日志功能
 * 
 * @author scf-loan
 */
@Slf4j
@Component
public class BusinessJobHandler extends AbstractJobHandler {

    @Autowired
    private DemoJobService demoJobService;

    /**
     * 处理Demo业务任务
     * 
     * @param param 任务参数
     * @return 执行结果
     * @throws Exception 异常
     */
    @XxlJob("demoBusinessJob")
    public ReturnT<String> executeDemoBusiness(String param) throws Exception {
        return executeInternal(param, "demoBusiness");
    }

    /**
     * 数据同步任务
     * 
     * @param param 任务参数（JSON格式，包含数据ID等）
     * @return 执行结果
     * @throws Exception 异常
     */
    @XxlJob("dataSyncJob")
    public ReturnT<String> executeDataSync(String param) throws Exception {
        return executeInternal(param, "dataSync");
    }

    @Override
    protected ReturnT<String> doExecute(String param, String jobMethod) throws Exception {
        log.info("开始处理业务任务，方法：{}，参数：{}", jobMethod, param);
        
        if ("demoBusiness".equals(jobMethod)) {
            return executeDemoBusinessLogic(param);
        } else if ("dataSync".equals(jobMethod)) {
            return executeDataSyncLogic(param);
        }
        
        log.error("未知的任务方法：{}", jobMethod);
        return ReturnT.FAIL;
    }

    /**
     * 执行Demo业务逻辑
     * 
     * @param param 参数
     * @return 执行结果
     * @throws Exception 异常
     */
    private ReturnT<String> executeDemoBusinessLogic(String param) throws Exception {
        log.info("开始执行Demo业务逻辑，参数：{}", param);
        
        // 调用业务服务
        boolean result = demoJobService.executeDemoBusiness(param);
        
        if (result) {
            log.info("Demo业务任务处理成功");
            // 记录业务指标
            recordBusinessCounter("demo.business.processed.count");
            recordBusinessMetric("demo.business.success.rate", 1);
            return ReturnT.SUCCESS;
        } else {
            log.error("Demo业务任务处理失败");
            recordBusinessMetric("demo.business.success.rate", 0);
            return ReturnT.FAIL;
        }
    }

    /**
     * 执行数据同步逻辑
     * 
     * @param param 参数
     * @return 执行结果
     * @throws Exception 异常
     */
    private ReturnT<String> executeDataSyncLogic(String param) throws Exception {
        log.info("开始执行数据同步逻辑，参数：{}", param);
        
        // 解析参数
        Long dataId = null;
        if (param != null && !param.trim().isEmpty()) {
            try {
                dataId = Long.parseLong(param.trim());
            } catch (NumberFormatException e) {
                log.warn("参数格式错误，使用默认参数：{}", param);
            }
        }
        
        // 如果参数为空，使用默认数据ID
        if (dataId == null) {
            dataId = 1L;
        }
        
        // 获取数据
        String data = demoJobService.getDemoData(dataId);
        log.info("数据同步完成，数据：{}", data);
        
        // 记录业务指标
        recordBusinessCounter("data.sync.processed.count");
        recordBusinessMetric("data.sync.processed.data.size", data != null ? data.length() : 0);
        
        return ReturnT.SUCCESS;
    }
}