package com.scf.loan.web.handler;

import com.scf.loan.common.dto.ResponseDTO;
import com.scf.loan.common.enums.ResultCodeEnum;
import com.scf.loan.common.exception.ServiceException;
import com.scf.loan.common.exception.ValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseDTO<?> handleServiceException(HttpServletRequest request, ServiceException e) {
        log.warn("业务异常: {}-{}", e.getMessage(), e.getBusinessMessage(), e);
        return ResponseDTO.fail(e.getResultCode(), e.getBusinessMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(ValidateException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseDTO<?> handleValidateException(HttpServletRequest request, ValidateException e) {
        log.warn("参数校验异常: {}", e.getMessage(), e);
        return ResponseDTO.fail(ResultCodeEnum.PARAM_ERROR, ResultCodeEnum.PARAM_ERROR.getMessage() + ":" + e.getMessage());
    }


    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<?> handleException(HttpServletRequest request, Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseDTO.fail(ResultCodeEnum.SYSTEM_ERROR);
    }
}
