package com.example.demo.common.exception;

public class UrlExistException extends RuntimeException {
    public UrlExistException() {
        super("Url存在");
    }
}
