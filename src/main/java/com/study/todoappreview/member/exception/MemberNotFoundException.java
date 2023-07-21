package com.study.todoappreview.member.exception;

public class MemberNotFoundException extends IllegalArgumentException{

    public MemberNotFoundException() {
        super("MEMBER NOT FOUND");
    }
}
