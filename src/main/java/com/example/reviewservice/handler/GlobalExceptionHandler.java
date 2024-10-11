package com.example.reviewservice.handler;

import com.example.reviewservice.controller.response.Response;
import com.example.reviewservice.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public Response<Void> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return Response.error(ErrorCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return Response.error(ErrorCode.INTERNAL_ERROR);
    }
}
