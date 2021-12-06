package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.entity.BoxEntity;
import com.example.demo.entity.DrawingEntity;
import com.example.demo.entity.LabelEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-10-12 15:15:17
 */
public interface LabelService extends IService<LabelEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void insertLabel(String label, Integer drawingId);

    List<LabelEntity> selectByDrawingId (Integer id);

    List<LabelEntity> selectAll ();

    List<LabelEntity> selectByLabel(String label);

    List<LabelEntity> selectByDrawingIds(List<Integer> ids);

    void deleteByIds(Integer[] drawingIds);

}

