package com.scf.loan.common.dto;

import lombok.Data;

import java.util.List;
import java.io.Serializable;

@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<T> records;
    private long total;
    private int page;
    private int size;

    public static <T> PageResult<T> of(List<T> records, long total, int page, int size) {
        PageResult<T> pr = new PageResult<>();
        pr.setRecords(records);
        pr.setTotal(total);
        pr.setPage(page);
        pr.setSize(size);
        return pr;
    }
}
