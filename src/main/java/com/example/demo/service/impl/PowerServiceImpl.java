package com.example.demo.service.impl;

import com.example.demo.entity.DrawingEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.Query;

import com.example.demo.dao.PowerDao;
import com.example.demo.entity.PowerEntity;
import com.example.demo.service.PowerService;


@Service("powerService")
public class PowerServiceImpl extends ServiceImpl<PowerDao, PowerEntity> implements PowerService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PowerEntity> page = this.page(
                new Query<PowerEntity>().getPage(params),
                new QueryWrapper<PowerEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PowerEntity selectById(Integer id) {
        QueryWrapper<PowerEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        return getBaseMapper().selectOne(wrapper);
    }

}