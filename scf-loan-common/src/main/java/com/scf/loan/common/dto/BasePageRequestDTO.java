package com.scf.loan.common.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 分页查询基础请求DTO
 * <p>
 * 提供通用的分页参数和常用的查询条件字段
 * </p>
 */
@Data
public class BasePageRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码，默认第一页
     */
    private Integer page = 1;

    /**
     * 每页大小，默认10条
     */
    private Integer size = 10;

    /**
     * 主键ID，用于精确查询
     */
    private Long id;

    /**
     * 开始时间，用于时间范围查询
     */
    private LocalDateTime startTime;

    /**
     * 结束时间，用于时间范围查询
     */
    private LocalDateTime endTime;
}
