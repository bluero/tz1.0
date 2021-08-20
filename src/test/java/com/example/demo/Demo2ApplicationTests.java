package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Demo2ApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
//        User user=new User(4l,"admin","admin","admin",null,"1","2");
//        userService.deleteById(user);
//        User user=new User();
//        user.setUser_id(2l);
//        user.setCreate_time(null);
//        user.setPassword_md5("1111");
//        userMapper.insert(user);
        User user=new User(null,"admin","admin","",null,"","");
        userService.insertUser(user);

    }

}
