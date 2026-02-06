package com.scf.loan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回结果枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    SUCCESS("200", "成功"),
    PARAM_ERROR("400", "参数错误"),
    SYSTEM_ERROR("500", "系统异常"),
    BUSINESS_ERROR("600", "业务异常");

    private final String code;
    private final String message;
}
