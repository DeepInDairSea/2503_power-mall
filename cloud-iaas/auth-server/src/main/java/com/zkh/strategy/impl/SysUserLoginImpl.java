package com.zkh.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkh.domain.LoginSysUser;
import com.zkh.mapper.LoginSysUserMapper;
import com.zkh.model.SecurityUserDetailsImpl;
import com.zkh.strategy.LoginStrategyService;
import com.zkh.constant.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service(value = AuthConstants.SYS_USER_LOGIN)
@CacheConfig(cacheNames = "com.zkh.strategy.impl.SysUserLoginImpl")
public class SysUserLoginImpl implements LoginStrategyService {
    @Autowired
    private LoginSysUserMapper loginSysUserMapper;

    @Override
    @Cacheable(key = "#username")
    public UserDetails getUserDetails(String username) {
//        LoginSysUser loginSysUser1 = new LoginSysUser();
//        String username1 = loginSysUser1.getUsername();
        //查询user
        LoginSysUser loginSysUser = loginSysUserMapper.selectOne(new LambdaQueryWrapper<LoginSysUser>().
            eq(LoginSysUser::getUsername, username));
        //查询用户权限
        Set<String> perms = loginSysUserMapper.selectPermsByLoginSysUserId(loginSysUser.getUserId());


        //封装SecurityUserDetails对象
        /***
         *         SecurityUserDetails securityUserDetails = new SecurityUserDetails();
         *         securityUserDetails.setUsername(loginSysUser.getUsername());
         *         securityUserDetails.setPassword(loginSysUser.getPassword());
         *         securityUserDetails.setUserId(loginSysUser.getUserId());
         *         securityUserDetails.setStatus(loginSysUser.getStatus());
         *         securityUserDetails.setShopId(loginSysUser.getShopId());
         *         securityUserDetails.setPerms(permsSet);
         *         securityUserDetails.setLoginType(AuthConstants.SYS_USER_LOGIN);
         *
         */

        SecurityUserDetailsImpl securityUserDetails = new SecurityUserDetailsImpl();
        if (ObjectUtil.isNotNull(loginSysUser)) {
//            SecurityUserDetailsImpl securityUserDetails = SecurityUserDetailsImpl.builder()
//                .userId(loginSysUser.getUserId())
//                .username(loginSysUser.getUsername())
//                .password(loginSysUser.getPassword())
//                .status(loginSysUser.getStatus())
//                .shopId(loginSysUser.getShopId())
//                .loginType(AuthConstants.SYS_USER_LOGIN)
//                .build();

            securityUserDetails.setUsername(loginSysUser.getUsername());
            securityUserDetails.setPassword(loginSysUser.getPassword());
            securityUserDetails.setUserId(loginSysUser.getUserId());
            securityUserDetails.setStatus(loginSysUser.getStatus());
            securityUserDetails.setShopId(loginSysUser.getShopId());
            securityUserDetails.setPerms(perms);
            securityUserDetails.setLoginType(AuthConstants.SYS_USER_LOGIN);
            if (CollectionUtil.isNotEmpty(perms) && perms.size() != 0) {
                securityUserDetails.setPerms(perms);
            }
            System.out.println("aaaaa");
            return securityUserDetails;
        }
        //用户为空 返回null
        return securityUserDetails;
    }
}
