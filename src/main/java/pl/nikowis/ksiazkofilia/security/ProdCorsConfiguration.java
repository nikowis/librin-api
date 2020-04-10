package pl.nikowis.ksiazkofilia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.nikowis.ksiazkofilia.config.Profiles;

@Configuration
@Profile(Profiles.PROD)
public class ProdCorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("https://nikowis.com", "http://89.65.58.225").allowedMethods("*").allowedHeaders("*").allowCredentials(true);
            }
        };
    }
}