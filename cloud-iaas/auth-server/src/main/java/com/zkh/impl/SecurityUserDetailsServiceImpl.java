package com.zkh.impl;

import com.zkh.constant.AuthConstants;
import com.zkh.factory.LoginTypeStrategyFactory;
import com.zkh.strategy.LoginStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Service
//@Slf4j
//@Component
@Configuration
public class SecurityUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private LoginTypeStrategyFactory loginTypeStrategyFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        System.out.println("aaaaaaaa");
//        log.error("触发了");
        //获取请求域
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        //获取请求头
        String loginType = requestAttributes.getRequest().getHeader(AuthConstants.LOGIN_TYPE);
//        String loginType = (String) requestAttributes.getAttribute(AuthConstants.LOGIN_TYPE, 1);
        System.out.println("loginType:" + loginType);

        if (!StringUtils.hasText(loginType)){
            throw new InternalAuthenticationServiceException("非法登录，登录类型未知");
        }
        //使用策略模式加工厂模式获得一个与前端登录类型匹配的
        LoginStrategyService loginInstance = loginTypeStrategyFactory.getLoginInstance(loginType);
        return loginInstance.getUserDetails(username);
    }
}
