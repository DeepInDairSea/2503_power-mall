package com.zkh.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * 登录统一响应结果对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("登录统一响应结果对象")
public class LoginResult {
    /**
     *
     */
    @ApiModelProperty("可用的token")
    private String accessToken;
    @ApiModelProperty("token有效时长")
    private Long expireTime;
}
