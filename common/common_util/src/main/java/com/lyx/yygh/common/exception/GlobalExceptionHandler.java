package com.lyx.yygh.common.exception;

import com.lyx.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(LyxException.class)
    @ResponseBody
    public Result error(LyxException e){
        return Result.build(e.getCode(), e.getMessage());
    }
}

