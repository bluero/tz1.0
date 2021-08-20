package com.example.demo.common.resp;

import com.example.demo.common.constants.Constants;
import lombok.Data;

@Data
public class ApiResult <T>{
    private int code= Constants.RESP_STATUS_OK;

    private String message;

    private T data;

    private  Object object;
}
