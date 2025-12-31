package com.modulink;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("azienda-logos");
        String uploadPath = uploadDir.toAbsolutePath().toUri().toString();
        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }
        registry.addResourceHandler("/azienda-logos/**")
                .addResourceLocations(uploadPath);
        uploadDir = Paths.get("user-logos");
        uploadPath = uploadDir.toAbsolutePath().toUri().toString();
        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }
        registry.addResourceHandler("/user-logos/**")
                .addResourceLocations(uploadPath);
    }
}
