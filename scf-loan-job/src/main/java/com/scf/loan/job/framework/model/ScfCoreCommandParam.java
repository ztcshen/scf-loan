package com.scf.loan.job.framework.model;

import java.util.Map;

public class ScfCoreCommandParam<T> {
    private String commandType;
    private String executeType;
    private String routeKey;
    private T businessParam;
    private Map<String, Object> ext;
    private String traceLogId;
    private Integer retryTimes;
    private Long timeout;

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public T getBusinessParam() {
        return businessParam;
    }

    public void setBusinessParam(T businessParam) {
        this.businessParam = businessParam;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    public String getTraceLogId() {
        return traceLogId;
    }

    public void setTraceLogId(String traceLogId) {
        this.traceLogId = traceLogId;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
