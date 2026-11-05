package com.example.expense_tracker

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration // this class contains app config
class CorsConfig {

    @Bean // registers CORS with Spring
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")                          // applies config to all endpoints
                    .allowedOrigins("http://localhost:5173")                    // allows requests from frontend
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // lists allowed methods
                    .allowedHeaders("*")                                        // allows any headers
            }
        }
    }
}