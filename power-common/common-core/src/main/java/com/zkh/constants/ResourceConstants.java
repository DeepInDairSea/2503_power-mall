package com.zkh.constants;


public interface ResourceConstants {
    String[] RESOURCE_ALLOW_URLS = {
//        "/doc.html",
//        "/swagger-resources/**",
//        "/swagger/**",
//        "/swagger-ui/**",
//        "/**/v2/api-docs",
//        "/**/v3/api-docs",
//        "/webjars/springfox-swagger-ui/**",
//        "/webjars/**",
//        "/druid/**",
//        "/actuator/**",
//        "/**/*.js",
//        "/**/*.css",
//        "/**/*.png",
//        "/**/*.ico",
            "/v2/api-docs",  // swagger
            "/v3/api-docs",
            "/swagger-resources/configuration/ui",  //用来获取支持的动作
            "/swagger-resources",                   //用来获取api-docs的URI
            "/swagger-resources/configuration/security",//安全选项
            "/webjars/**",
            "/swagger-ui/**",
            "/druid/**",
            "/actuator/**"
    };


}
