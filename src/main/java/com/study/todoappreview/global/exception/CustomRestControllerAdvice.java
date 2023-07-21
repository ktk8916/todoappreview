package com.study.todoappreview.global.exception;

import com.study.todoappreview.global.auth.exception.NoTokenException;
import com.study.todoappreview.global.login.domain.response.ExceptionResponse;
import com.study.todoappreview.member.exception.DuplicateEmailException;
import com.study.todoappreview.member.exception.LoginFailException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomRestControllerAdvice {

    @ExceptionHandler(LoginFailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String loginFailExceptionHandler(LoginFailException e){
        return e.getMessage();
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String duplicateEmailExceptionHandler(DuplicateEmailException e){
        return e.getMessage();
    }

    @ExceptionHandler(WeakKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse weakKeyExceptionHandler(WeakKeyException e){
        return ExceptionResponse.of("잘못된 토큰 요청", e.getCause());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse expiredJwtExceptionHandler(ExpiredJwtException e){
        return ExceptionResponse.of("만료된 토큰", e.getCause());
    }
    @ExceptionHandler(NoTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse noTokenExceptionHandler(NoTokenException e){
        return ExceptionResponse.of("토큰이 비어있습니다", e.getCause());
    }
}
