package com.example.demo.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.example.demo.common.exception.DrawingNameExistException;
import com.example.demo.common.exception.UrlExistException;
import com.example.demo.common.exception.UserNameExistException;
import com.example.demo.common.utils.rr;
import com.example.demo.dao.User1Dao;
import com.example.demo.entity.User1Entity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.Query;

import com.example.demo.dao.DrawingDao;
import com.example.demo.entity.DrawingEntity;
import com.example.demo.service.DrawingService;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.common.constants.Constants.*;


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

    @Override
    public String uploadDrawing(MultipartFile multipartFile) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 最后上传生成的文件名
        String finalFileName = System.currentTimeMillis() + "" + new SecureRandom().nextInt(0x0400) + suffixName;
        // oss中的文件夹名
        String objectName = sdf.format(new Date()) + "/" + finalFileName;
        //获取user 得到原来的头像地址
        // 调用阿里云
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建上传文件的元信息，可以通过文件元信息设置HTTP header(设置了才能通过返回的链接直接访问)。
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称
        ossClient.putObject("yuanjunli", objectName, new ByteArrayInputStream(multipartFile.getBytes()));
        // 设置URL过期时间为1小时。
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        String url = ossClient.generatePresignedUrl("yuanjunli", objectName, expiration).toString();
        // 关闭OSSClient。
        ossClient.shutdown();

        return url;
    }

    @Override
    public List<DrawingEntity> selectByText(String name) {
        QueryWrapper<DrawingEntity> wrapper = new QueryWrapper<>();
        wrapper.like("drawing_name", name);
        return getBaseMapper().selectList(wrapper);
    }

    @Override
    public List<DrawingEntity> selectBy(Long id) {
        QueryWrapper<DrawingEntity> wrapper = new QueryWrapper<>();
        wrapper.like("drawing_url", "jpg");
        return getBaseMapper().selectList(wrapper);
    }

    @Override
    public List<DrawingEntity> selectByIds(List<Integer> ids) {
        QueryWrapper<DrawingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("drawing_id", ids);
        return getBaseMapper().selectBatchIds(ids);
    }

    public DrawingEntity selectById(Integer id) {
        QueryWrapper<DrawingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("drawing_id", id);
        return getBaseMapper().selectOne(wrapper);
    }

    @Override
    public void checkUrlUnique(String Url) throws UrlExistException {
        DrawingDao baseMapper = this.baseMapper;
        Integer count = baseMapper.selectCount(new QueryWrapper<DrawingEntity>().eq("drawing_url", Url));
        if (count > 0) {
            throw new UrlExistException();
        }
    }

    @Override
    public void checkDrawingNameUnique(String drawingName) throws DrawingNameExistException {
        DrawingDao baseMapper = this.baseMapper;
        Integer count = baseMapper.selectCount(new QueryWrapper<DrawingEntity>().eq("drawing_name", drawingName));
        if (count > 0) {
            throw new DrawingNameExistException();
        }
    }

    @Override
    public List<DrawingEntity> selectByLabel(String name) {
        QueryWrapper<DrawingEntity> wrapper = new QueryWrapper<>();
        wrapper.like("drawing_label", "name");
        return getBaseMapper().selectList(wrapper);
    }

    @Override
    public void insertData() {
        for (int i = 0; i < 1000; i++) {
            String a = "";
            for (int j = 0; j < 8; j++) {
                a = a + rr.getRandomChar();
            }
            DrawingEntity drawing = new DrawingEntity();
            drawing.setDrawingName("" + a + ".jpg");
            getBaseMapper().insert(drawing);
        }
    }


}