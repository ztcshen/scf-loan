package com.scf.loan.job.framework.trace;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class TraceIdGenerator {
    private static final AtomicLong SEQ = new AtomicLong(0);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String generateRootId(String systemCode, String taskType) {
        String sys = format(systemCode, 2, "SC");
        String date = LocalDate.now().format(DATE_FORMATTER);
        String task = format(taskType, 4, "TASK");
        long seq = SEQ.incrementAndGet() % 100000000L;
        return sys + date + task + String.format("%08d", seq);
    }

    public static String generateSplitId(String rootId, String level, String shardId, String businessId) {
        String lvl = format(level, 2, "00");
        String shard = formatNumeric(shardId, 4, "0000");
        String biz = businessId == null ? "" : businessId;
        return rootId + "-" + lvl + "-" + shard + (biz.isBlank() ? "" : "-" + biz);
    }

    private static String format(String value, int length, String fallback) {
        String v = value == null || value.isBlank() ? fallback : value;
        if (v.length() >= length) {
            return v.substring(0, length);
        }
        return String.format("%1$-" + length + "s", v).replace(' ', '0');
    }

    private static String formatNumeric(String value, int length, String fallback) {
        String v = value == null || value.isBlank() ? fallback : value;
        if (v.length() >= length) {
            return v.substring(0, length);
        }
        return String.format("%1$" + length + "s", v).replace(' ', '0');
    }
}
