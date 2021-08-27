package com.example.demo.controller;

import com.example.demo.common.resp.ApiResult;
import com.example.demo.entity.User;
import com.example.demo.exception.ProjectException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    public String test1(){
        return "login";
    }

//    @RequestMapping("/login1")
//    public String login1(User user){
//        User user1=userService.selectByLoginName(user);
//        System.out.println(user1);
//        if(user1==null||!user1.getPasswordMd5().equals(user.getPasswordMd5())){
//            System.out.println("密码或用户名错误，请重新输入");
//            return "login";
//        }
//        else{
//            System.out.println("登录成功");
//            return "login";
//        }
//
//    }

    @RequestMapping("login2")
    @ResponseBody
    public ApiResult<User> login2( User user){
        ApiResult<User> resp=new ApiResult<>();
        try{
            String data=user.getLoginName();
            if(StringUtils.isBlank(data)){
                throw new ProjectException("参数校验错误");
            }
            User resource=userService.selectByLoginName(data);
            resp.setData(resource);
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        return resp;
    }

    @RequestMapping("register")
    public String register(){
        return "register";
    }

    @RequestMapping("add")
    @ResponseBody
    public ApiResult<User> add(User user){
        ApiResult<User> resp=new ApiResult<>();
        userService.insertUser(user);
        resp.setMessage("ok");
        return resp;
    }

    @RequestMapping("queryUserByLoginName")
    @ResponseBody
    public ApiResult<User> queryUserByLoginName(String loginName ){
        ApiResult<User> resp=new ApiResult<>();
        User user1=userService.selectByLoginName(loginName);
        resp.setData(user1);
        return resp;

    }

    @RequestMapping("showAllUser")
    @ResponseBody
    public ApiResult<List<User>> showAllUser(){
        ApiResult<List<User>> resp=new ApiResult<>();
        List<User> userList=userMapper.selectList(null);
        System.out.println(userList);
        resp.setData(userList);
        resp.setMessage("OK");
        return resp;
    }

    @RequestMapping("deleteUser")
    @ResponseBody
    public void deleteUser(long userId){

        userService.deleteById(userId);
        return;
    }

    @RequestMapping("updatePass")
    @ResponseBody
    public void updatePass(){

    }

    @RequestMapping("assignPermissions")
    @ResponseBody
    public void assignPermissions(long userId){

    }

}
