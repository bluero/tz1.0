package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-08-26 16:51:41
 */
@Data
@TableName("tz_drawing")
public class DrawingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer drawingId;
	/**
	 * 
	 */
	private String drawingUrl;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Long createUser;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private String drawingName;
	/**
	 * 
	 */
	private String drawingLabel;

}
