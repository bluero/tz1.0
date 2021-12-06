package com.example.demo.service.impl;

import com.example.demo.entity.LabelEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.Query;

import com.example.demo.dao.FeatureDao;
import com.example.demo.entity.FeatureEntity;
import com.example.demo.service.FeatureService;


@Service("featureService")
public class FeatureServiceImpl extends ServiceImpl<FeatureDao, FeatureEntity> implements FeatureService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FeatureEntity> page = this.page(
                new Query<FeatureEntity>().getPage(params),
                new QueryWrapper<FeatureEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteByIds(Integer[] drawingIds) {
        List<Integer> integers = Arrays.asList(drawingIds);
        QueryWrapper<FeatureEntity> wrapper = new QueryWrapper<>();
        wrapper.in("drawing_id",drawingIds);
        getBaseMapper().delete(wrapper);

    }

}