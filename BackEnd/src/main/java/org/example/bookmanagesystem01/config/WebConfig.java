package org.example.bookmanagesystem01.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration //说明是全局注解
public class WebConfig extends WebMvcConfigurationSupport {  //跨域配置类

    @Override
    public void addCorsMappings(CorsRegistry registry){
        /**
         * 1.与访问请求
         * 2.请求来源
         * 3.方法
         * 4.允许携带token
         * 5.响应最大时间
         */
        registry.addMapping("/**")
                .allowedOrigins("Http://localhost:8080","null")
                .allowedMethods("GET","POST","PUT","OPTIONS","DELETE")
                .allowCredentials(true)
                .maxAge(3600);
    }

}
