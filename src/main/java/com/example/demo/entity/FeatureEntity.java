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
 * @date 2021-10-12 15:15:17
 */
@Data
@TableName("tz_feature")
public class FeatureEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer drawingId;
	/**
	 * 
	 */
	private String drawingGlobalFeature;
	/**
	 * 
	 */
	private String drawingUpFeature;
	/**
	 * 
	 */
	private String drawingDownFeature;
	/**
	 * 
	 */
	private String drawingLeftFeature;
	/**
	 * 
	 */
	private String drawingRightFeature;

}
