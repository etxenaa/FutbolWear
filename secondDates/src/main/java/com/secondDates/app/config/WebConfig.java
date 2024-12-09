package com.secondDates.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Asegura que Spring Boot sigue sirviendo archivos CSS, JS y otros recursos estáticos
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        // Configura el manejo de las imágenes dentro de la carpeta /uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:src/main/resources/static/uploads/"); // Ruta absoluta donde se guardan las imágenes
    }
}
