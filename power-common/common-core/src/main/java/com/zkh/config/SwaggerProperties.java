package com.zkh.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "swagger3")
public class SwaggerProperties {
    private String basePackage;
    private String name;
    private String url;
    private String email;
    private String title;
    private String description;
    private String license;
    private String licenseUrl;
    private String termsOfServiceUrl;
    private String version;
}
