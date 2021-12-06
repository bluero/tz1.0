package com.example.demo.mapper;

import com.example.demo.entity.DrawingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LabelMapper {
    List<DrawingEntity> selectLabelList(List<String> strings);

    List<DrawingEntity> selectTextAndLabelList(@Param("name") String name, @Param("list") List<String> strings);

    List<DrawingEntity> selectByName(String name);
}
