package Ex.Digital.Api.ExdigitalApi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir imágenes desde la carpeta static/img
        registry
            .addResourceHandler("/img/**")
            .addResourceLocations("classpath:/static/img/")
            .setCachePeriod(3600);
    }
}