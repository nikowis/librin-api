package pl.nikowis.ksiazkofilia.config;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableScheduling
public class BaseConfiguration {

    @Autowired
    private MessageSource messageSource;

    @Bean
    public MapperFacade mapperFacade() {
        return MapperConfiguration.mapperFactory().getMapperFacade();
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}