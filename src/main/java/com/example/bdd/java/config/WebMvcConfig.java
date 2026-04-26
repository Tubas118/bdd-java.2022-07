package com.example.bdd.java.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${application.cors-allowed-origins}")
    private String corsAllowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (StringUtils.hasLength(corsAllowedOrigins)) {
            registry.addMapping("/**")
                    .allowedOrigins(corsAllowedOrigins)
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowCredentials(true);;
        }
    }

}
