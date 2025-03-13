package com.zkh.strategy;


import org.springframework.security.core.userdetails.UserDetails;

//@Service
public interface LoginStrategyService {
    UserDetails getUserDetails(String username);
}
