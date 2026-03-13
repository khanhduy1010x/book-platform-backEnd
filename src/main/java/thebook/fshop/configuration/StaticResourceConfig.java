package thebook.fshop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:/var/www/uploads/avatars/");

        registry.addResourceHandler("/coverBook/**")
                .addResourceLocations("file:/var/www/uploads/coverBook/");

        registry.addResourceHandler("/EpubFile/**")
                .addResourceLocations("file:/var/www/uploads/EpubFile/");

        registry.addResourceHandler("/banner/**")
                .addResourceLocations("file:/var/www/uploads/banner/");

        registry.addResourceHandler("/author/**")
                .addResourceLocations("file:/var/www/uploads/author/");
    }
}