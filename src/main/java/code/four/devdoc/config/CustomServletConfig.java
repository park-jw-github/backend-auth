package code.four.devdoc.config;

import code.four.devdoc.controller.formatter.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addFormatter(new LocalDateFormatter());
    }
/*
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
               .allowedOrigins("*", "https://kec5d57735d81a.user-app.krampoline.com")
               .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
               .maxAge(300)
               .allowedHeaders("Authorization", "Cache-Control", "Content-Type");
     }*/

}
