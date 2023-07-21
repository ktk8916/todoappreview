package com.study.todoappreview.member.exception;

public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException() {
        super("EXISTS EMAIL");
    }
}
