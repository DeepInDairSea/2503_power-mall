package com.zkhpower.constant;

public enum BusinessEnum {
    operation_fail(-1,"操作失败"),
    SERVER_INNER_FAIL(9999,"服务器内部错误"),
    UNAUTHORIZED(403,"未授权"),
    ACCESS_DENY_FAIL(400,"访问被拒绝")
    ;
//    响应状态码
    public Integer code;
//    响应消息
    public String message;

    BusinessEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
