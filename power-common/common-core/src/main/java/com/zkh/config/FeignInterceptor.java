package com.zkh.config;

import cn.hutool.core.util.ObjectUtil;
import com.zkh.constant.AuthConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class FeignInterceptor implements RequestInterceptor {



    @Override
    public void apply(RequestTemplate requestTemplate) {
        //获取当前请求的token
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNotNull(requestAttributes)) {
            //获取请求对象
            HttpServletRequest request = requestAttributes.getRequest();
            if (ObjectUtil.isNotNull(request)) {
                //获取token
                String token = request.getHeader(AuthConstants.AUTHORIZATION);
                //传递token
                requestTemplate.header(AuthConstants.AUTHORIZATION, token);
            }
        }
        //如果是原本就不会有token的请求，比如定时器发送的请求等，就给一个固定的token值
        requestTemplate.header(AuthConstants.AUTHORIZATION, AuthConstants.REDIS_LOGIN_PREFIX+"33a12c66-8b46-4a36-be66-0d4275a944b5");
    }
}
