package com.zkh.constant;

public interface AuthConstants {
    //请求头存放token的字段名称
    String AUTHORIZATION = "Authorization";
    //token的格式前缀
    String BEARER = "bearer ";
    //
    String REDIS_LOGIN_PREFIX = "login_token: ";
    //登录url
//    String LOGIN_URL = "/auth-server/doLogin";
    String LOGIN_URL = "/doLogin";
    //登出url
    String LOGOUT_URL = "/doLogout";
    //请求头登录方式标志
    String LOGIN_TYPE = "loginType";
    //商城购物系统用户登录方式标志
    String MEMBER_LOGIN = "memberLogin";
    //商城管理系统用户登录方式标志
    String SYS_USER_LOGIN = "sysUserLogin";
    //token过期时间、6小时
    Long EXPIRE_TIME = 21600L;
    //token续约阈值、1小时
    Long REDIS_EXPIRED_THRESHOLD_TIME = 3600L;
}
