package com.example.demo.common.exception;

public class UserNameExistException extends RuntimeException {

    public UserNameExistException() {
        super("用户名存在");
    }
}
