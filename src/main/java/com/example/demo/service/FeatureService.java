package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.entity.FeatureEntity;

import java.util.Map;

/**
 * 
 *
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-10-12 15:15:17
 */
public interface FeatureService extends IService<FeatureEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void deleteByIds(Integer[] drawingIds);
}

