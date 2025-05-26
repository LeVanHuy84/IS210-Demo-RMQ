package io.messagequeue.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapping từ URL /images/** đến thư mục file thật uploads/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/");
    }
}
