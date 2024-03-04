package com.hg.webflux.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

/**
 * @description 全局异常处理
 * @Author ygl
 * @Create 2024/3/1 09:52
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ArithmeticException.class)
    public String error(ArithmeticException exception) {
        System.out.println("ArithmeticException:" + Arrays.toString(exception.getStackTrace()));
        return "ArithmeticException!";
    }
}
