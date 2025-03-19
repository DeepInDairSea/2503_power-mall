package com.zkh.filter;

import com.alibaba.fastjson.JSONObject;
import com.zkh.constant.AuthConstants;
import com.zkh.model.SecurityUserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class TokenTranslationFilter extends OncePerRequestFilter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String auth = request.getHeader(AuthConstants.AUTHORIZATION);
        //去除前缀bearer
        String token = auth.replaceFirst(AuthConstants.BEARER, "");
        //判断token是否有值
        if (StringUtils.hasText(token)) {
            //token续签问题
            Long expire = stringRedisTemplate.getExpire(AuthConstants.REDIS_LOGIN_PREFIX + token);
            if (expire < AuthConstants.REDIS_EXPIRED_THRESHOLD_TIME){
                //给token续签，本质是更改redis中token的有效时长
                stringRedisTemplate.expire(AuthConstants.REDIS_LOGIN_PREFIX+token,AuthConstants.EXPIRE_TIME, TimeUnit.SECONDS);
            }

            //从redis中获取token对应的用户信息
//        Set<String> keys = stringRedisTemplate.keys(AuthConstants.REDIS_LOGIN_PREFIX + token);
            String userInfo = stringRedisTemplate.opsForValue().get(AuthConstants.REDIS_LOGIN_PREFIX + token);
            // 反向序列化：字符串转换成安全框架能认识的用户对象
            JSONObject jsonObject = new JSONObject();
            SecurityUserDetailsImpl securityUserDetails = jsonObject.parseObject(userInfo, SecurityUserDetailsImpl.class);
            //
            Set<SimpleGrantedAuthority> collect = securityUserDetails.getPerms().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            //新建一个Authentication对象
            Authentication authentication = new UsernamePasswordAuthenticationToken(securityUserDetails,null,collect);

            //放入security框架的上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
