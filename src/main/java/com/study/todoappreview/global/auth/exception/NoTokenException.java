package com.study.todoappreview.global.auth.exception;

public class NoTokenException extends RuntimeException{

    public NoTokenException() {
        super("TOKEN IS NULL");
    }
}
