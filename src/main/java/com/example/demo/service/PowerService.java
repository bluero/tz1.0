package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.entity.DrawingEntity;
import com.example.demo.entity.PowerEntity;

import java.util.Map;

/**
 * 
 *
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-11-18 17:02:46
 */
public interface PowerService extends IService<PowerEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PowerEntity selectById(Integer id);
}

