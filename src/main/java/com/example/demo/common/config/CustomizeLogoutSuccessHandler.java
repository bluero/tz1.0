package com.example.demo.common.config;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.utils.R;
import com.example.demo.entity.User1Entity;
import com.example.demo.service.User1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private User1Service user1Service;
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

//        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User1Entity user1 = user1Service.selectByLoginName(userDetails.getUsername());
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(R.ok()));
    }

}
