package com.scf.loan.web.handler;

import com.scf.loan.common.base.dto.ResponseDTO;
import com.scf.loan.common.base.enums.ResultCodeEnum;
import com.scf.loan.common.base.exception.ServiceException;
import com.scf.loan.common.base.exception.ValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO<?> handleServiceException(ServiceException e) {
        log.warn("业务异常: {}-{}", e.getMessage(), e.getBusinessMessage(), e);
        return ResponseDTO.fail(e.getResultCode(), e.getBusinessMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(ValidateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO<?> handleValidateException(ValidateException e) {
        log.warn("参数校验异常: {}", e.getMessage(), e);
        return ResponseDTO.fail(ResultCodeEnum.PARAM_ERROR, ResultCodeEnum.PARAM_ERROR.getMessage() + ":" + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .orElse(ResultCodeEnum.PARAM_ERROR.getMessage());
        log.warn("请求参数校验异常: {}", message, e);
        return ResponseDTO.fail(ResultCodeEnum.PARAM_ERROR, ResultCodeEnum.PARAM_ERROR.getMessage() + ":" + message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO<?> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .orElse(ResultCodeEnum.PARAM_ERROR.getMessage());
        log.warn("约束校验异常: {}", message, e);
        return ResponseDTO.fail(ResultCodeEnum.PARAM_ERROR, ResultCodeEnum.PARAM_ERROR.getMessage() + ":" + message);
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<?> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseDTO.fail(ResultCodeEnum.SYSTEM_ERROR);
    }
}
