package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.Query;

import com.example.demo.dao.DrawingDao;
import com.example.demo.entity.DrawingEntity;
import com.example.demo.service.DrawingService;


@Service("drawingService")
public class DrawingServiceImpl extends ServiceImpl<DrawingDao, DrawingEntity> implements DrawingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DrawingEntity> page = this.page(
                new Query<DrawingEntity>().getPage(params),
                new QueryWrapper<DrawingEntity>()
        );

        return new PageUtils(page);
    }

}