package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.User;

public interface UserService extends IService<User> {

    User selectByLoginName(String loginName);

    void deleteById(long user);

    void insertUser(User user);

}
