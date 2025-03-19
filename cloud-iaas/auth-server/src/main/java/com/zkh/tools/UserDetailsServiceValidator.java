//package com.zkh.tools;
//
//import com.zkh.impl.SecurityUserDetailsServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserDetailsServiceValidator implements CommandLineRunner {
//
//    private final SecurityUserDetailsServiceImpl securityUserDetailsServiceImpl;
//
//    @Autowired
//    public UserDetailsServiceValidator(SecurityUserDetailsServiceImpl securityUserDetailsServiceImpl) {
//        this.securityUserDetailsServiceImpl = securityUserDetailsServiceImpl;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (securityUserDetailsServiceImpl != null) {
//            System.out.println("SecurityUserDetailsServiceImpl is loaded successfully.");
//        } else {
//            System.out.println("SecurityUserDetailsServiceImpl is not loaded.");
//        }
//    }
//}
