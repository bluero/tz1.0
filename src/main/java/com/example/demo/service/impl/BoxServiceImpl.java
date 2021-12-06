package com.example.demo.service.impl;

import com.example.demo.entity.DrawingEntity;
import com.example.demo.entity.LabelEntity;
import com.example.demo.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.Query;

import com.example.demo.dao.BoxDao;
import com.example.demo.entity.BoxEntity;
import com.example.demo.service.BoxService;


@Service("boxService")
public class BoxServiceImpl extends ServiceImpl<BoxDao, BoxEntity> implements BoxService {



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BoxEntity> page = this.page(
                new Query<BoxEntity>().getPage(params),
                new QueryWrapper<BoxEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<BoxEntity> selectByUserID(Long id) {
        QueryWrapper<BoxEntity> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",id);
        return getBaseMapper().selectList(wrapper);
    }

    @Override
    public List<BoxEntity> selectAll() {
        QueryWrapper<BoxEntity> wrapper=new QueryWrapper<>();
        return getBaseMapper().selectList(wrapper);
    }




}