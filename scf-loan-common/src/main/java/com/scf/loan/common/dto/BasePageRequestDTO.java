package com.scf.loan.common.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.io.Serializable;

@Data
public class BasePageRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer page = 1;
    private Integer size = 10;
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
