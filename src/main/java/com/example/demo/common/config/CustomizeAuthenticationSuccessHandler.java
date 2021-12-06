package com.example.demo.common.config;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.utils.R;
import com.example.demo.entity.User1Entity;
import com.example.demo.service.User1Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private User1Service user1Service;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User1Entity user1 = user1Service.selectByLoginName(userDetails.getUsername());
        HttpSession session = httpServletRequest.getSession();
        httpServletResponse.getWriter().write(JSON.toJSONString(R.ok().put("user",user1)));
        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展

    }
}
