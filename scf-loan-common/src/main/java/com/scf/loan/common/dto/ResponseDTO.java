package com.scf.loan.common.dto;

import com.scf.loan.common.enums.ResultCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应对象
 */
@Data
public class ResponseDTO<T> implements Serializable {

    private String code;
    private String message;
    private T data;

    public static <T> ResponseDTO<T> success() {
        return success(null);
    }

    public static <T> ResponseDTO<T> success(T data) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(ResultCodeEnum.SUCCESS.getCode());
        response.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> ResponseDTO<T> fail(ResultCodeEnum resultCode) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(resultCode.getCode());
        response.setMessage(resultCode.getMessage());
        return response;
    }

    public static <T> ResponseDTO<T> fail(String code, String message) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
    
    public static <T> ResponseDTO<T> fail(ResultCodeEnum resultCode, String message) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(resultCode.getCode());
        response.setMessage(message);
        return response;
    }
}
