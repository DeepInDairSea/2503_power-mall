package com.zkh.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkh.configuration.WhiteAllowUrl;
import com.zkh.constant.AuthConstants;
import com.zkh.constant.BusinessEnum;
import com.zkh.constant.HttpConstants;
import com.zkh.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {
    //自动注入白名单
    @Autowired
    private WhiteAllowUrl whiteAllowUrl;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求头
        ServerHttpRequest request = exchange.getRequest();
        //获取path
        String path = request.getPath().toString();
        System.out.println("path:" + path);
        //引入远程配置文件的白名单,并判断是否放行
        if (whiteAllowUrl.getAllowUrls().contains(path)) {
            //请求path，包含在白名单中，放行
            return chain.filter(exchange);
        }
        //白名单未包含请求路径,需要验证token
        //从约定的位置获取token bearer token
        String auth = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION);
        //去掉请求的token前缀，获取token
        String token = auth.replaceFirst(AuthConstants.BEARER,"");
        //查看是否有值、redis中是否有值
        if (StringUtils.hasText(token) && stringRedisTemplate.hasKey(AuthConstants.REDIS_LOGIN_PREFIX+token)) {
            //有值，且token存在于redis中
            return chain.filter(exchange);
        }
        //没有token或者redis中没有此token
        log.error("非法请求,时间：{},请求ip：{}",new Date(),path);
        //返回响应错误信息
        ServerHttpResponse response = exchange.getResponse();
        //设置响应头
        response.getHeaders().set(HttpConstants.CONTENT_TYPE,HttpConstants.APPLICATION_JSON);
        //设置响应信息
        Result<Object> result = Result.fail(BusinessEnum.UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
//        byte[] bytes = new byte[0];
        byte[] bytes;
        try {
             bytes = objectMapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return -5;
    }
}
