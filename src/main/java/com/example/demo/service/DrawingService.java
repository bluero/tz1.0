package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.entity.DrawingEntity;

import java.util.Map;

/**
 * 
 *
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-08-26 16:51:41
 */
public interface DrawingService extends IService<DrawingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

