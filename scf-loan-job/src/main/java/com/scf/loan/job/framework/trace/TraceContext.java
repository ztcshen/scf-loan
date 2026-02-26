package com.scf.loan.job.framework.trace;

import org.slf4j.MDC;

public class TraceContext {
    private static final ThreadLocal<TraceContextData> CONTEXT = new ThreadLocal<>();

    public static String initRoot(String systemCode, String taskType) {
        String rootId = TraceIdGenerator.generateRootId(systemCode, taskType);
        TraceContextData data = new TraceContextData(rootId, rootId);
        CONTEXT.set(data);
        MDC.put("rootId", rootId);
        MDC.put("splitId", rootId);
        return rootId;
    }

    public static String split(String level, String shardId, String businessId) {
        TraceContextData data = CONTEXT.get();
        if (data == null) {
            return null;
        }
        String splitId = TraceIdGenerator.generateSplitId(data.rootId, level, shardId, businessId);
        data.splitId = splitId;
        MDC.put("splitId", splitId);
        return splitId;
    }

    public static void clearSplit() {
        TraceContextData data = CONTEXT.get();
        if (data != null) {
            data.splitId = data.rootId;
            MDC.put("splitId", data.rootId);
        }
    }

    public static void clear() {
        CONTEXT.remove();
        MDC.remove("rootId");
        MDC.remove("splitId");
    }

    public static String getRootId() {
        TraceContextData data = CONTEXT.get();
        return data == null ? null : data.rootId;
    }

    public static String getSplitId() {
        TraceContextData data = CONTEXT.get();
        return data == null ? null : data.splitId;
    }

    private static class TraceContextData {
        private final String rootId;
        private String splitId;

        private TraceContextData(String rootId, String splitId) {
            this.rootId = rootId;
            this.splitId = splitId;
        }
    }
}
