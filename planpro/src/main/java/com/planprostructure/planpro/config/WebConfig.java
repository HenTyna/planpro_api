package com.planprostructure.planpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Bean
    public HandlerExceptionResolver customHandlerExceptionResolver() {  // Changed method name
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.setProperty("Exception", "error/500");
        mappings.setProperty("RuntimeException", "error/500");
        resolver.setExceptionMappings(mappings);
        resolver.setDefaultErrorView("error/default");
        resolver.setExceptionAttribute("ex");
        resolver.setWarnLogCategory("planpro.error");
        return resolver;
    }

}
