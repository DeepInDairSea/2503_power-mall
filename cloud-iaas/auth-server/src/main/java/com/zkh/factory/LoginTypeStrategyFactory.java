package com.zkh.factory;

import com.zkh.strategy.LoginStrategyService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginTypeStrategyFactory {

    @Resource
//    @Autowired
    private Map<String,LoginStrategyService> loginStrategyServiceMap = new HashMap<>();

    public LoginStrategyService getLoginInstance(String loginType){
        return loginStrategyServiceMap.get(loginType);
    };
}
