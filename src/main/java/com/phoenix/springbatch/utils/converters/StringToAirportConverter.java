package com.phoenix.springbatch.utils.converters;

import com.phoenix.springbatch.service.enums.Airport;
import org.springframework.core.convert.converter.Converter;

public class StringToAirportConverter implements Converter<String, Airport> {

    @Override
    public Airport convert(String source) {
        return Airport.valueOf(source);
    }
}
