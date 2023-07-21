package com.study.todoappreview.member.exception;

public class LoginFailException extends RuntimeException{

    public LoginFailException() {
        super("LOGIN FAIL");
    }
}
