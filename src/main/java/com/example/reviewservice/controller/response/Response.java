package com.example.reviewservice.controller.response;

import com.example.reviewservice.exception.ErrorCode;

public record Response<T>(Integer code, String message, T value){

    public static <T> Response<T> ok(T value){
        return new Response<>(200, "OK", value);
    }

    public static <T> Response<T> error(ErrorCode errorCode){
        return new Response<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
}
