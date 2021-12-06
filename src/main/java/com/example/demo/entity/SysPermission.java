package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_permission")
public class SysPermission {

    private static final long serialVersionUID = -71969734644822184L;
    //主键id
    private Integer id;
    //权限code
    private String permissionCode;
    //权限名
    private String permissionName;

}
