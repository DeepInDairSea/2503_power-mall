package com.zkh.util;

import com.zkh.model.SecurityUserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

/**
 * 获取认证授权UserDetails工具类
 */
public class AuthUtils {
    /**
     * 获取security上下文的SecurityUserDetailsImpl对象
     * @return
     */
    public static SecurityUserDetailsImpl getUserDetails(){
        return (SecurityUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    /**
     * 获取security上下文的SecurityUserDetailsImpl对象的用户标识
     */
    public static Long getUserId(){
        return getUserDetails().getUserId();
    }

    /**
     * 获取security中认证对象的操作权限集合
     * @return
     */
    public static Set<String> getUserPerms(){

        return getUserDetails().getPerms();
    }
}
