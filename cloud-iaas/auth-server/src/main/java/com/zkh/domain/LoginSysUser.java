package com.zkh.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
    * 系统用户
    */
//@ApiModel(value="com-zkh-domain-SysUser")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class LoginSysUser implements Serializable {
    @TableId(value = "user_id", type = IdType.AUTO)
//    @ApiModelProperty(value="")
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "username")
//    @ApiModelProperty(value="用户名")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
//    @ApiModelProperty(value="密码")
    private String password;


    /**
     * 状态  0：禁用   1：正常
     */
    @TableField(value = "status")
//    @ApiModelProperty(value="状态  0：禁用   1：正常")
    private Integer status;



    /**
     * 用户所在的商城Id
     */
    @TableField(value = "shop_id")
//    @ApiModelProperty(value="用户所在的商城Id")
    private Long shopId;

    private static final long serialVersionUID = 1L;
}