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
 * @date 2021-11-18 17:02:46
 */
@Data
@TableName("tz_power")
public class PowerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long userId;
	/**
	 * 
	 */
	private Integer userPower;
	/**
	 * 
	 */
	private Integer userCount;
	/**
	 * 
	 */
	private Integer retrievePower;
	/**
	 * 
	 */
	private Integer retrieveCount;
	/**
	 * 
	 */
	private Integer matchPower;
	/**
	 * 
	 */
	private Integer matchCount;
	/**
	 * 
	 */
	private Integer uploadPower;
	/**
	 * 
	 */
	private Integer uploadCount;
	/**
	 * 
	 */
	private Integer drawingPower;
	/**
	 * 
	 */
	private Integer drawingCount;
	/**
	 * 
	 */
	private Integer systemPower;
	/**
	 * 
	 */
	private Integer systemCount;
	/**
	 * 
	 */
	private Integer personPower;
	/**
	 * 
	 */
	private Integer personCount;
	/**
	 * 
	 */
	private Integer outputPower;
	/**
	 * 
	 */
	private Integer outputCount;

}
