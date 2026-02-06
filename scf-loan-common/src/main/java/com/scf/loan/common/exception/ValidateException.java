package com.scf.loan.common.exception;

/**
 * 参数校验异常
 */
public class ValidateException extends RuntimeException {

    public ValidateException(String message) {
        super(message);
    }
}
