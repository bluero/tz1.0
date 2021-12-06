package com.example.demo.common.exception;

public class DrawingNameExistException extends RuntimeException{

    public DrawingNameExistException() {
        super("图纸名存在");
    }
}
