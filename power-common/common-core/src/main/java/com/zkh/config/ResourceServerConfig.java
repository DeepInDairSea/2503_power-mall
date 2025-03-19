package com.zkh.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkh.constant.BusinessEnum;
import com.zkh.constant.HttpConstants;
import com.zkh.constants.ResourceConstants;
import com.zkh.filter.TokenTranslationFilter;
import com.zkh.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenTranslationFilter tokenTranslationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭跨域网站伪造
        http.csrf().disable();
        //关闭资源共享
        http.cors().disable();
        //关闭session策略
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //配置一个token解析过滤器，将token转换成安全框架认识的用户信息，再存放security框架的上下文当中
        http.addFilterBefore(tokenTranslationFilter, UsernamePasswordAuthenticationFilter.class);

        //配置处理携带token但是权限不足的请求
        http.exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())   //处理没有携带token的请求
            .accessDeniedHandler(accessDeniedHandler());                                 //处理携带token但是权限不足的情况
        http.authorizeRequests()
            .antMatchers(ResourceConstants.RESOURCE_ALLOW_URLS)
            .permitAll()
            .anyRequest().authenticated();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType(HttpConstants.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstants.CHAR_SET);

            //封装成一个结果对象
            Result<Object> result = Result.fail(BusinessEnum.UNAUTHORIZED);
            ObjectMapper objectMapper = new ObjectMapper();
            String resultValueAsString = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(resultValueAsString);
            writer.flush();
            writer.close();
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType(HttpConstants.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstants.CHAR_SET);

            Result<Object> result = Result.fail(BusinessEnum.ACCESS_DENY_FAIL);
            ObjectMapper objectMapper = new ObjectMapper();
            String resultValueAsString = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(resultValueAsString);
            writer.flush();
            writer.close();
        };
    }
}
