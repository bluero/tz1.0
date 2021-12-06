package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-10-12 15:15:17
 */
@Data
@TableName("tz_box")
public class BoxEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer boxId;
    /**
     *
     */
    private String drawing1Url;
    /**
     *
     */
    private String drawing2Url;
    /**
     *
     */
    private Long userId;

    private Date createTime;

}
