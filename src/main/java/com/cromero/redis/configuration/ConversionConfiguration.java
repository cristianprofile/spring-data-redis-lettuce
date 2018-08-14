package com.cromero.redis.configuration;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversionConfiguration {


    // to be able to use @Value Spring boot 2 property conversion. They were expecting this feature out of the box
    // for pring-boot 2.1.0.
    //https://docs.spring.io/spring-boot/docs/2.0.4RELEASE/reference/htmlsingle/#boot-features-external-config-conversion-duration
    @Bean
    public ApplicationConversionService conversionService()
    {
        final ApplicationConversionService applicationConversionService = new ApplicationConversionService();
        return applicationConversionService;
    }


}
