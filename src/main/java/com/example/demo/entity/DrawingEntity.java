package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.example.demo.common.valid.AddGroup;
import com.example.demo.common.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
	@TableId(type = IdType.AUTO)
	@NotNull(message = "修改必须指定id",groups = {UpdateGroup.class})
	@Null(message = "新增不能指定id",groups = {AddGroup.class})
	private Integer drawingId;
	/**
	 * 
	 */
	@URL
	@NotBlank(message = "用户名不能为空",groups = {AddGroup.class, UpdateGroup.class})
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
