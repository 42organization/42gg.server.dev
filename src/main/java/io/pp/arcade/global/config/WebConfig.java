package io.pp.arcade.global.config;

import io.pp.arcade.global.type.CodeToEnumConverterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final String baseUrl = "localhost:3000";

    /* cors */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false).maxAge(3600);
    }

    /* convert type */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CodeToEnumConverterFactory());
    }
}