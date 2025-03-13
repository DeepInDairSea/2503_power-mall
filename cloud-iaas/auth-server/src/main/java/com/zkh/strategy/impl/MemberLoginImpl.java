package com.zkh.strategy.impl;

import com.zkh.strategy.LoginStrategyService;
import com.zkh.constant.AuthConstants;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service(value=AuthConstants.MEMBER_LOGIN)
public class MemberLoginImpl implements LoginStrategyService {
    @Override
    public UserDetails getUserDetails(String username) {
        return null;
    }
}
