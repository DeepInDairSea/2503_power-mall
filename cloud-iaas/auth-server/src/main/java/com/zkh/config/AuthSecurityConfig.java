package com.zkh.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkh.constant.AuthConstants;
import com.zkh.constant.BusinessEnum;
import com.zkh.constant.HttpConstants;
import com.zkh.impl.SecurityUserDetailsServiceImpl;
import com.zkh.model.LoginResult;
import com.zkh.model.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.UUID;


@Configuration
@EnableWebSecurity
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
    @Resource
    private SecurityUserDetailsServiceImpl securityUserDetailsServiceImpl;
//    @Autowired
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
//        SecurityUserDetailsImpl securityUserDetails = new SecurityUserDetailsImpl();
        auth.userDetailsService(securityUserDetailsServiceImpl).passwordEncoder(bCryptPasswordEncoder());
        System.out.println("securityUserDetailsImpl:" + securityUserDetailsServiceImpl);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        //关闭跨域请求
        http.csrf().disable();
//        //关闭跨站请求资源共享
//        http.cors().disable();
        //要求所有请求都要走这个认证流程
        http.authorizeRequests().anyRequest().authenticated();
        //关闭session使用策略
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //登录配置
        http.formLogin().loginProcessingUrl(AuthConstants.LOGIN_URL)
            .successHandler(authenticationSuccessHandler())
            .failureHandler(authenticationFailureHandler());

//        登出配置
        http.logout().logoutUrl(AuthConstants.LOGOUT_URL)
            .logoutSuccessHandler(logoutSuccessHandler());
        // 关闭跨站请求伪造
//        http.cors().disable();
//        http.cors().disable();
        // 关闭跨域请求
//        http.csrf().disable();
        // 关闭session使用策略
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        // 配置登录信息
//        http.formLogin()
//            .loginProcessingUrl(AuthConstants.LOGIN_URL)// 设置登录URL
//            .successHandler(authenticationSuccessHandler())   // 设置登录成功处理器
//            .failureHandler(authenticationFailureHandler());  // 调协登录失败处理器
//
//        // 配置登出信息
//        http.logout()
//            .logoutUrl(AuthConstants.LOGOUT_URL)// 设置登出URL
//            .logoutSuccessHandler(logoutSuccessHandler());// 设置登出成功处理器
//
//        // 要求所有请求都需要进行身份的认证
//        http.authorizeHttpRequests().anyRequest().authenticated();

//        http.authorizeRequests().anyRequest().authenticated().and()
//            .formLogin()
//            .loginProcessingUrl(AuthConstants.LOGIN_URL)// 设置登录URL
//            .successHandler(authenticationSuccessHandler())   // 设置登录成功处理器
//            .failureHandler(authenticationFailureHandler())
//            .and().httpBasic()
//            .and().logout().
//            logoutUrl(AuthConstants.LOGOUT_URL)
//            .logoutSuccessHandler(logoutSuccessHandler());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            //设置响应类型和响应字符集
            response.setContentType(HttpConstants.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstants.CHAR_SET);
            //封装、颁发token
            //Principal就是安全框架的UserDetails对象\L利用fastjson转换为json字符串
            String userJsonString = JSONObject.toJSONString(authentication.getPrincipal());

            String uuid = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set(AuthConstants.REDIS_LOGIN_PREFIX + uuid, userJsonString
                , Duration.ofSeconds(AuthConstants.EXPIRE_TIME));
            //封装统一响应结果对象
            LoginResult loginResult = new LoginResult(uuid, AuthConstants.EXPIRE_TIME);
            //封装响应对象
            //Result<String> result = new Result<>();
            Result<LoginResult> result = Result.success(loginResult);
            //返回结果
            ObjectMapper objectMapper = new ObjectMapper();
            String writeValueAsString = objectMapper.writeValueAsString(result);
            //转换为
            //响应
            PrintWriter writer = response.getWriter();
            writer.write(writeValueAsString);
            writer.flush();
            writer.close();

        };
    }


    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            //设置响应头
            response.setContentType(HttpConstants.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstants.CHAR_SET);
            //判断登录异常类型并封装到统一响应对象
//            Result<String> result = new Result<String>();
            Result<String> result = new Result<>();
            if (exception instanceof BadCredentialsException) {
                result.setMsg("错误的凭证");
            } else if (exception instanceof CredentialsExpiredException) {
                result.setMsg("凭证过期");
            } else if (exception instanceof LockedException) {
                result.setMsg("账户被锁定、联系管理员");
            } else if (exception instanceof UsernameNotFoundException) {
                result.setMsg("账户不存在、联系管理员");
            } else if (exception instanceof InternalAuthenticationServiceException) {
                result.setMsg("内部认证服务异常、联系管理员");
            } else if (exception instanceof DisabledException) {
                result.setMsg("账户不可用、联系管理员");
            } else if (exception instanceof AccountExpiredException) {
                result.setMsg("账户过期、联系管理员");
            } else {
                result.setMsg(BusinessEnum.OPERATION_FAIL.getMessage());
            }
            //包装
            ObjectMapper objectMapper = new ObjectMapper();
            String resultString = objectMapper.writeValueAsString(result);
            //响应
            PrintWriter writer = response.getWriter();
            writer.write(resultString);
            writer.flush();
            writer.close();
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType(HttpConstants.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstants.CHAR_SET);
            //查询当前token
            String authToken = request.getHeader(AuthConstants.AUTHORIZATION);
            String token = authToken.replaceFirst(AuthConstants.BEARER, "");
            //删除redis token
            stringRedisTemplate.delete(AuthConstants.REDIS_LOGIN_PREFIX + token);

            //封装结果对象
            Result<Object> logoutSuccessResult = Result.success(null);
            ObjectMapper objectMapper = new ObjectMapper();
            String logoutSuccessResultString = objectMapper.writeValueAsString(logoutSuccessResult);
            PrintWriter writer = response.getWriter();
            writer.write(logoutSuccessResultString);
            writer.flush();
            writer.close();
        };
    }

    //security框架要求必须要有密码加密器
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(Arrays.asList("*"));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//        config.setAllowedHeaders(Arrays.asList("*"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsWebFilter((CorsConfigurationSource) source);
//    }
}
