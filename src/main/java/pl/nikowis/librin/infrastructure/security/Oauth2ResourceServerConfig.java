package pl.nikowis.librin.infrastructure.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import pl.nikowis.librin.application.rest.MainController;
import pl.nikowis.librin.application.rest.OffersController;
import pl.nikowis.librin.application.rest.PolicyController;

@Configuration
@EnableResourceServer
public class Oauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().ignoringAntMatchers("/ws/**").disable()//todo enable?
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/email").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers(OffersController.OFFERS_ENDPOINT + "/**").permitAll()
                .antMatchers(MainController.REGISTRATION_ENDPOINT).permitAll()
                .antMatchers(PolicyController.POLICIES_ENDPOINT + "/**").permitAll();
    }
}