package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.example.demo.common.valid.AddGroup;
import com.example.demo.common.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 
 * 
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-08-30 16:14:01
 */
@Data
@TableName("tz_user")
public class User1Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@NotNull(message = "修改必须指定id",groups = {UpdateGroup.class})
	@Null(message = "新增不能指定id",groups = {AddGroup.class})
	@TableId(type = IdType.AUTO)
	private Long userId;
	/**
	 * 
	 */
	@NotBlank(message = "用户名不能为空",groups = {AddGroup.class, UpdateGroup.class})
	private String loginName;
	/**
	 * 
	 */
	private String nickName;
	/**
	 * 
	 */
	private String passwordMd5;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String userGroup;
	/**
	 * 
	 */
	private String userPower;

}
