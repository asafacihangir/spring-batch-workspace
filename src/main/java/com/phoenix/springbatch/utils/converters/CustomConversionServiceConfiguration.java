package com.phoenix.springbatch.utils.converters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class CustomConversionServiceConfiguration {

    @Bean
    public ConversionService conversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(conversionService);
        conversionService.addConverter(new DateToLocalDateConverter());
        conversionService.addConverter(new StringToAirportConverter());
        return conversionService;
    }
}
