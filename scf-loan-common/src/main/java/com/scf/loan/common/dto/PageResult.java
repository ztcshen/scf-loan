package com.scf.loan.common.dto;

import lombok.Data;

import java.util.List;
import java.io.Serializable;

/**
 * 通用分页结果封装类
 *
 * @param <T> 结果集元素类型
 */
@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页大小
     */
    private int size;

    /**
     * 构造分页结果
     *
     * @param records 数据列表
     * @param total   总记录数
     * @param page    当前页码
     * @param size    每页大小
     * @return 分页结果对象
     */
    public static <T> PageResult<T> of(List<T> records, long total, int page, int size) {
        PageResult<T> pr = new PageResult<>();
        pr.setRecords(records);
        pr.setTotal(total);
        pr.setPage(page);
        pr.setSize(size);
        return pr;
    }
}
