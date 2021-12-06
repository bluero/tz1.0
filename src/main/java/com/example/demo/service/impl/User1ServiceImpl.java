package com.example.demo.service.impl;

import com.example.demo.common.exception.UserNameExistException;
import com.example.demo.service.User1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.Query;

import com.example.demo.dao.User1Dao;
import com.example.demo.entity.User1Entity;


@Service("userService")
public class User1ServiceImpl extends ServiceImpl<User1Dao, User1Entity> implements User1Service {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<User1Entity> page = this.page(
                new Query<User1Entity>().getPage(params),
                new QueryWrapper<User1Entity>()
        );

        return new PageUtils(page);
    }

    @Override
    public User1Entity selectByLoginName(String loginName) {
        QueryWrapper<User1Entity> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name", loginName);
        return getBaseMapper().selectOne(wrapper);
    }

    @Override
    public void checkUserNameUnique(String username) throws UserNameExistException {
        User1Dao baseMapper = this.baseMapper;
        Integer count = baseMapper.selectCount(new QueryWrapper<User1Entity>().eq("login_name", username));
        if (count > 0) {
            throw new UserNameExistException();
        }


    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNameExistException {
        //需要构造出 org.springframework.security.core.userdetails.User 对象并返回
        if (username == null || "".equals(username)) {
            throw new RuntimeException("用户不能为空");
        }
        User1Entity user1 = selectByLoginName(username);
        if (user1 == null) {
            throw new RuntimeException("用户不存在");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user1 != null) {
            //获取该用户所拥有的权限
//            List<SysPermission> sysPermissions = sysPermissionService.selectListByUser(sysUser.getId());
            // 声明用户授权
//            sysPermissions.forEach(sysPermission -> {
//                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysPermission.getPermissionCode());
//                grantedAuthorities.add(grantedAuthority);
//            });
        }


        return null;
    }

    @Override
    public User1Entity selectByUserId(Integer userId) {

        return getBaseMapper().selectById(userId);
    }


}