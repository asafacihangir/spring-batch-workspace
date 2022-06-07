package com.phoenix.springbatch.utils;

import java.time.*;
import java.util.Date;

public class DateUtils {

  public static Date toDate(LocalDate localDate) {
    Instant instant = LocalDateTime.of(localDate, LocalTime.MIDNIGHT)
        .toInstant(ZoneOffset.UTC);
    return new Date(instant.toEpochMilli());
  }

  public static LocalDate toLocalDate(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        .toLocalDate();
  }

}
