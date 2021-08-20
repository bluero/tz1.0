package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectByLoginName(String loginName) {
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("login_name",loginName);
        User user1=userMapper.selectOne(wrapper);
        return user1;
    }

    @Override
    public void deleteById(long userId) {
//        QueryWrapper<User> wrapper=new QueryWrapper<>();
//        wrapper.eq("login_name",user.getLoginName());
//        User user1=userMapper.selectOne(wrapper);
        userMapper.deleteById(userId);
    }

    @Override
    public void insertUser(User user) {
        userMapper.insert(user);
    }

    private String generateToken(User user) {
        String source=user.getUserId()+":"+user.getLoginName()+":"+System.currentTimeMillis();
        return source;
    }
}
