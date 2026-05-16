package com.liubo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author 68
 * 2026/5/15 22:54
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 允许所有域名进行跨域调用
        config.addAllowedOriginPattern("*");

        // 允许所有请求头
        config.addAllowedHeader("*");

        // 允许所有请求方法
        config.addAllowedMethod("*");

        // 允许携带凭证
        config.setAllowCredentials(true);

        // 预检请求的缓存时间（秒）
        config.setMaxAge(3600L);

        // 允许暴露的响应头
        config.addExposedHeader("Content-Disposition");
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}