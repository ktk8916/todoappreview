package com.study.todoappreview.global.login.domain.response;

public record ExceptionResponse(String message, Throwable cause) {

    public static ExceptionResponse of(String message, Throwable cause){
        return new ExceptionResponse(message, cause);
    }
}
