package com.zkh.model;

import com.zkh.constant.BusinessEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("项目统一响应结果对象")
public class Result<T> implements Serializable {
    @ApiModelProperty("状态码")
    private Integer code = 200;
    @ApiModelProperty("消息")
    private String msg = "successful";
    @ApiModelProperty("数据")
    private T data;

    /**
     * 相应成功的方法
     * @param data 返回的数据
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data){
        Result result = new Result<>();
//        result.setCode(200);
//        result.setMsg("successful");
        result.setData(data);
        return result;
    }

    /***
     * 响应失败的方法
     * @param code
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> Result<T> fail(Integer code, String msg){
        Result result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     *响应失败使用枚举类
     * @param businessEnum
     * @return
     * @param <T>
     */
    public static <T> Result<T> fail(BusinessEnum businessEnum){
        Result result = new Result<>();
        result.setCode(businessEnum.getCode());
        result.setMsg(businessEnum.getMessage());
        result.setData(null);
        return result;
    }
}
