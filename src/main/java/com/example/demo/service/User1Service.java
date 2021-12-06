package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.exception.UserNameExistException;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.entity.User1Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

/**
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-08-30 16:14:01
 */
public interface User1Service extends IService<User1Entity> {

    PageUtils queryPage(Map<String, Object> params);

    User1Entity selectByLoginName(String loginName);

    void checkUserNameUnique(String username) throws UserNameExistException;

    UserDetails loadUserByUsername(String username) throws UserNameExistException;

    User1Entity selectByUserId(Integer userId);

}

