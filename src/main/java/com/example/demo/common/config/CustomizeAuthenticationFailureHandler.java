package com.example.demo.common.config;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.utils.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException, ServletException {
        httpServletResponse.setContentType("text/json;charset=utf-8");
        if (e instanceof BadCredentialsException) {
            httpServletResponse.getWriter().write(JSON.toJSONString(R.error(500, "密码错误")));
        } else if (e instanceof InternalAuthenticationServiceException) {
            httpServletResponse.getWriter().write(JSON.toJSONString(R.error(400, "用户不存在")));
        } else if (e instanceof SessionAuthenticationException) {
            httpServletResponse.getWriter().write(JSON.toJSONString(R.error(515, "该用户多次登录")));
        } else {
            httpServletResponse.getWriter().write(JSON.toJSONString(R.error(202, "登录错误")));
        }

    }
}
