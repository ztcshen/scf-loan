package com.scf.loan.common.base.exception;

import com.scf.loan.common.base.enums.ResultCodeEnum;

/**
 * 参数校验异常
 */
public class ValidateException extends RuntimeException {

    public ValidateException(String message) {
        super(message);
    }
}
