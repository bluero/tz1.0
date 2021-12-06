package com.example.demo.service.impl;

import com.example.demo.dao.DrawingDao;
import com.example.demo.entity.BoxEntity;
import com.example.demo.entity.DrawingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.Query;

import com.example.demo.dao.LabelDao;
import com.example.demo.entity.LabelEntity;
import com.example.demo.service.LabelService;


@Service("labelService")
public class LabelServiceImpl extends ServiceImpl<LabelDao, LabelEntity> implements LabelService {

    @Autowired
    private LabelService labelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LabelEntity> page = this.page(
                new Query<LabelEntity>().getPage(params),
                new QueryWrapper<LabelEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void insertLabel(String label, Integer drawingId) {
        if (label != null&&!label.equals("")) {
            String[] str2 = label.split(",");
            for (String s : str2) {
                LabelEntity labelEntity = new LabelEntity();
                LabelDao baseMapper = this.baseMapper;
                Integer count = baseMapper.selectCount(new QueryWrapper<LabelEntity>().eq("drawing_id", drawingId).eq("drawing_label", s));
                labelEntity.setDrawingId(drawingId);
                if (count == 0) {
                    labelEntity.setDrawingLabel(s);
                }
                labelService.save(labelEntity);
            }
        }

    }

    @Override
    public List<LabelEntity> selectByDrawingId(Integer id) {
        QueryWrapper<LabelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("drawing_id", id);
        return getBaseMapper().selectList(wrapper);
    }

    @Override
    public List<LabelEntity> selectAll() {
        QueryWrapper<LabelEntity> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT drawing_label");
        return getBaseMapper().selectList(wrapper);
    }

    @Override
    public List<LabelEntity> selectByLabel(String label) {
        QueryWrapper<LabelEntity> wrapper = new QueryWrapper<>();
        wrapper.like("drawing_label", label);
        return getBaseMapper().selectList(wrapper);

    }

    @Override
    public List<LabelEntity> selectByDrawingIds(List<Integer> ids) {
        QueryWrapper<LabelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("drawing_id", ids);
        return getBaseMapper().selectBatchIds(ids);
    }

    @Override
    public void deleteByIds(Integer[] drawingIds) {
        List<Integer> integers = Arrays.asList(drawingIds);
        getBaseMapper().deleteBatchIds(integers);
    }


}