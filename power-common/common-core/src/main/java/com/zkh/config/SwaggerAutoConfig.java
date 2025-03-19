package com.zkh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;

@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)     //使用SwaggerProperties.class的属性
public class SwaggerAutoConfig {
    @Autowired
    private SwaggerProperties swaggerProperties;
    @Autowired
    private Environment environment;        //spring 自由的环境对象可以获取

    @Bean
    public Docket api() {
        Boolean flag = true;
        String[] activeProfiles = environment.getActiveProfiles();
        String activeProfile = activeProfiles[0];
        if ("pro".equals(activeProfile)){       //如果是生产环境则不生成api文档
            flag = false;
        }
        Docket docket = new Docket(DocumentationType.OAS_30).apiInfo(getApiInfo())
            .enable(flag)
            .select()
            .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))      //告诉swagger扫描那个包
            .build()
            ;
        return docket;
    }

    private ApiInfo getApiInfo() {
        Contact contact = new Contact(swaggerProperties.getName(), swaggerProperties.getUrl(), swaggerProperties.getEmail());
        ApiInfo apiInfo = new ApiInfo(
            swaggerProperties.getTitle(),
            swaggerProperties.getDescription(),
            swaggerProperties.getVersion(),
            swaggerProperties.getTermsOfServiceUrl(),
            contact,
            swaggerProperties.getLicense(),
            swaggerProperties.getLicenseUrl(),
            new HashSet<>()
        );
        return apiInfo;
    }
}
