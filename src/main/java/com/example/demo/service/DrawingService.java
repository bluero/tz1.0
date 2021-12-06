package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.exception.DrawingNameExistException;
import com.example.demo.common.exception.UrlExistException;
import com.example.demo.common.exception.UserNameExistException;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.entity.DrawingEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-08-26 16:51:41
 */
public interface DrawingService extends IService<DrawingEntity> {

    PageUtils queryPage(Map<String, Object> params);

    String uploadDrawing(MultipartFile file) throws IOException;

    List<DrawingEntity> selectByText(String name);

    List<DrawingEntity> selectBy(Long id);

    List<DrawingEntity> selectByIds(List<Integer> ids);

    DrawingEntity selectById(Integer id);

    void checkUrlUnique(String Url) throws UrlExistException;

    void checkDrawingNameUnique(String drawingName) throws DrawingNameExistException;

    List<DrawingEntity> selectByLabel(String name);

    void insertData();

}

