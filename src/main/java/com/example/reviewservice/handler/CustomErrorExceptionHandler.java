package com.example.reviewservice.handler;

import com.example.reviewservice.controller.response.Response;
import com.example.reviewservice.exception.CustomErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Integer.MIN_VALUE)
@RestControllerAdvice
public class CustomErrorExceptionHandler {

    @ExceptionHandler(value = CustomErrorException.class)
    public Response customException(CustomErrorException e) {
        log.error("[error] error: {}, message: {}", e.getErrorCode() , e.getMessage());

        return Response.error(e.getErrorCode());
    }
}
