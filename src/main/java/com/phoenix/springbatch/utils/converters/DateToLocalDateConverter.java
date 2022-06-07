package com.phoenix.springbatch.utils.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class DateToLocalDateConverter implements Converter<Date, LocalDate> {

    @Override
    public LocalDate convert(Date source) {
        return LocalDate.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }
}
