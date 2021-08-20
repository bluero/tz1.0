package com.example.demo.exception;

import com.example.demo.common.constants.Constants;

public class ProjectException extends Exception {

    public ProjectException(String message) {
        super(message);
    }

    public int getStatusCode() {
        return Constants.RESP_STATUS_INTERNAL_ERROR;
    }
}
