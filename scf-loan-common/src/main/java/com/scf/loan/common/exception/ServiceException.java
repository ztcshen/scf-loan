package com.scf.loan.common.exception;

import com.scf.loan.common.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class ServiceException extends RuntimeException {

    private String resultCode;
    private String businessMessage;

    public ServiceException(String message) {
        super(message);
        this.resultCode = ResultCodeEnum.BUSINESS_ERROR.getCode();
        this.businessMessage = message;
    }

    public ServiceException(ResultCodeEnum resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode.getCode();
        this.businessMessage = resultCode.getMessage();
    }

    public ServiceException(String code, String message) {
        super(message);
        this.resultCode = code;
        this.businessMessage = message;
    }
    
    public ServiceException(ResultCodeEnum resultCode, String message) {
        super(message);
        this.resultCode = resultCode.getCode();
        this.businessMessage = message;
    }
}
