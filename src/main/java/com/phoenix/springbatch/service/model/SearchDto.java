package com.phoenix.springbatch.service.model;

import com.phoenix.springbatch.service.enums.Airport;
import java.time.LocalDate;

public class SearchDto {

  public LocalDate flightDate;
  public Airport departureAirport;
  public Airport arrivalAirport;

}
