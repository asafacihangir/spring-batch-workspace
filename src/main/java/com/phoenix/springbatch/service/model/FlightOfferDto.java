package com.phoenix.springbatch.service.model;

import com.phoenix.springbatch.domain.FlightSearchResult;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FlightOfferDto {

  public FlightOfferDto(FlightSearchResult result) {
    this.airlineName = result.getAirlineName();
    this.price = result.getPrice();
    this.arrivalTime = result.getArrivalTime();
    this.departureTime = result.getDepartureTime();
  }

  public String airlineName;
  public BigDecimal price;
  public LocalDateTime departureTime;
  public LocalDateTime arrivalTime;

}
