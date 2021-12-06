package com.example.demo.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;


import com.aliyun.dysmsapi20170525.models.QuerySendDetailsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.example.demo.common.resp.ApiResult;
import com.example.demo.common.sms.Sample;
import com.example.demo.common.valid.AddGroup;
import com.example.demo.common.valid.UpdateGroup;
import com.example.demo.exception.ProjectException;
import com.example.demo.service.PowerService;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.generators.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.User1Entity;
import com.example.demo.service.User1Service;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


/**
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-08-30 16:14:01
 */
@RestController
@RequestMapping("/user")
public class User1Controller {
    @Autowired
    private User1Service user1Service;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    private PowerService powerService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RedisTemplate<Object, Object> objectRedisTemplate;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions(":user:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = user1Service.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
//    @RequiresPermissions(":user:info")
    public R info(@PathVariable("userId") Long userId) {
        User1Entity user = user1Service.getById(userId);
        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions(":user:save")
    public R save(@Validated({AddGroup.class}) String loginName, String loginer, String passwordMd5, String userPower) {
//        if(result.hasErrors()){
//            Map<String,String> map=new HashMap<>();
//            result.getFieldErrors().forEach((item)->{
//                String message=item.getDefaultMessage();
//                String field=item.getField();
//                map.put(field,message);
//            });
//            return R.error(400,"提交数据不合法").put("data",map);
//        }else{
        User1Entity user1 = user1Service.selectByLoginName(loginer);
        if (user1 != null && user1.getUserPower() != null && user1.getUserPower().equals("1")) {
            try {
                User1Entity user11 = new User1Entity();
                user11.setLoginName(loginName);
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                String encode = bCryptPasswordEncoder.encode(passwordMd5);
                user11.setPasswordMd5(encode);
                user11.setUserPower(userPower);
                user1Service.checkUserNameUnique(user11.getLoginName());
                user1Service.save(user11);
            } catch (Exception e) {
                return R.error(e.getMessage());
            }
        } else {
            return R.error(300, "该用户没有权限");
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions(":user:update")
    public R update(@Validated({UpdateGroup.class}) @RequestBody User1Entity user) {
        user1Service.updateById(user);
        return R.ok();
    }

    @RequestMapping("updateByUserName")
    public R updateById(String userName, String PasswordMd5) {
        User1Entity user1 = user1Service.selectByLoginName(userName);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pass = bCryptPasswordEncoder.encode(PasswordMd5);
        user1.setPasswordMd5(pass);
        user1Service.updateById(user1);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions(":user:delete")
    public R delete(@RequestBody Long[] userIds) {
        user1Service.removeByIds(Arrays.asList(userIds));
        return R.ok();
    }

    @RequestMapping("delete1")
    public R delete(Long userId) {
        try {
            user1Service.removeById(userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return R.ok();
    }

    @RequestMapping("login")
    @ResponseBody
    public R login2(User1Entity user) {
        try {
            String data = user.getLoginName();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (StringUtils.isBlank(data)) {
                throw new ProjectException("参数校验错误");
            }
            User1Entity resource = user1Service.selectByLoginName(data);
//            userDetailsService.loadUserByUsername(user.getLoginName());
            if (bCryptPasswordEncoder.matches(user.getPasswordMd5(), resource.getPasswordMd5())) {
                if (user.getUserGroup() != null && user.getUserGroup().equals(resource.getUserGroup())) {

                    if (user.getLoginName() != null && user.getLoginName().equals(redisTemplate.opsForValue().get(user.getLoginName()))) {
                        return R.error("该用户已经登录");
                    }
                    redisTemplate.opsForValue().set(user.getLoginName(), user.getLoginName());
                    Map<String, String> map = new HashMap<>();
                    User1Entity user1 = user1Service.selectByLoginName(user.getLoginName());
                    map.put("loginName", user1.getLoginName());
                    map.put("userPower", user1.getUserPower());
                    map.put("userGroup", user1.getUserGroup());
                    map.put("userId", user1.getUserId().toString());
                    return R.ok().put("user", map);
                } else {
                    return R.error("组织不正确");
                }

            } else {
                return R.error("密码或者用户名不正确");
            }
        } catch (ProjectException e) {
            e.printStackTrace();
            return R.error();
        }
    }

    @RequestMapping("logout")
    @ResponseBody
    public R logout(User1Entity user) {
        if (user.getLoginName().equals(redisTemplate.opsForValue().get(user.getLoginName())) && user.getLoginName() != null) {
            redisTemplate.delete(user.getLoginName());
            return R.ok(user.getLoginName()+"注销成功");
        }else {
            return R.error("该用户没有登录或者已经离线");
        }

    }

    @RequestMapping("sms")
    @ResponseBody
    public R sms() throws Exception {

//        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dysmsapi20170525.Client client = Sample.createClient("LTAI4Fdn27xpGqNVUFRguaAg", "HzR3qz1YgoQZNLPS4nZiU2jJkiY6LM");
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers("17314977069");
        sendSmsRequest.setSignName("急行共享汽车");
        sendSmsRequest.setTemplateCode("SMS_184631093");
        String code = UUID.randomUUID().toString().substring(0, 5);
        String phone = "17314977069";
        redisTemplate.opsForValue().set(phone, code, 10, TimeUnit.MINUTES);
        sendSmsRequest.setTemplateParam("{\"code\":\"" + code + "\"}");
        // 复制代码运行请自行打印 API 的返回值
        try {
            client.sendSms(sendSmsRequest);
        } catch (Exception e) {
            return R.error(233, e.getMessage());
        }

        return R.ok();
    }

    @RequestMapping("empower")
    public R empower(String userLoginName) {
        User1Entity user = user1Service.selectByLoginName(userLoginName);
        user.setUserPower("1");
        user1Service.updateById(user);
        return R.ok();
    }


}
