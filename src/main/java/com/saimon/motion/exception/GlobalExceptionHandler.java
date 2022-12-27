package com.saimon.motion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MotionException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleMotionException(MotionException ex) {

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        errorResponse.setDate(new Date());
        return errorResponse;
    }

    @ExceptionHandler(FileNotFound.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleFileNotFound(FileNotFound ex) {

        ErrorResponse errorResponse = new ErrorResponse("File not found");
        errorResponse.setDate(new Date());
        return errorResponse;
    }
}