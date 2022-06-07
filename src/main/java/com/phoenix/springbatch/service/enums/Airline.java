package com.phoenix.springbatch.service.enums;

import com.phoenix.springbatch.exception.AirlineRequestErrorException;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;

public enum Airline {
  TRANS_AMERICAN("Trans American Airline",5, 1000, 2000, 0, 600, 900, Airline.TRANS_AMERICAN_READER),
  ADIOS("Adios Airline",3, 2000, 1000, 0, 200, 300, Airline.ADIOS_READER),
  OCEANIC("Oceanic Airline",3, 2000, 1000, 0, 300, 500, Airline.OCEANIC_AIRLINE_READER),
  BELARUS("Belarus Airline",3, 2000, 1000, 0, 240, 500, Airline.BELARUS_AIRLINE_READER),
  SOUTH_PACIFIC("South Pacific Airline",3, 500, 1000, 0, 200, 300, Airline.SOUTH_PACIFIC_READER),
  FLY_US("Fly Us",3, 2010, 9500, 0, 170, 220, Airline.FLY_US_READER);

  public static final String TRANS_AMERICAN_READER = "transAmericanReader";
  public static final String ADIOS_READER = "adiosAirlineItemReader";
  public static final String OCEANIC_AIRLINE_READER = "oceanicAirlineItemReader";
  public static final String BELARUS_AIRLINE_READER = "belarusAirlineItemReader";
  public static final String SOUTH_PACIFIC_READER = "southPacificAirlineItemReader";
  public static final String FLY_US_READER = "flyUsAirlineItemReader";

  private final String airlineName;
  private final int baseDelayInMs;
  private final int failureRateInPercent;
  private final int priceFrom;
  private final int priceTo;
  private final int randomDelay;
  private final int nrOfOffers;
  private final String readerBeanName;

  Airline(String airlineName,
      int nrOfOffers,
      int baseDelayInMs,
      int randomDelay,
      int failureRateInPercent,
      int priceFrom,
      int priceTo,
      String readerBeanName) {
    this.airlineName = airlineName;
    this.nrOfOffers = nrOfOffers;
    this.baseDelayInMs = baseDelayInMs;
    this.failureRateInPercent = failureRateInPercent;
    this.priceFrom = priceFrom;
    this.priceTo = priceTo;
    this.randomDelay = randomDelay;
    this.readerBeanName = readerBeanName;
  }


  public String getAirlineName() {
    return airlineName;
  }

  public int getBaseDelayInMs() {
    return baseDelayInMs;
  }

  public int getFailureRateInPercent() {
    return failureRateInPercent;
  }

  public BigDecimal getRandomPrice() {
    return new BigDecimal(RandomUtils.nextInt(priceFrom, priceTo));
  }

  public Integer getRandomDelay() {
    return RandomUtils.nextInt(baseDelayInMs, baseDelayInMs + randomDelay);
  }

  public int getNrOfOffers() {
    return nrOfOffers;
  }

  public void simulateError() {
    if (failureRateInPercent == 0) {
      return;
    }

    int random = RandomUtils.nextInt(0, 100);
    if (random <= failureRateInPercent ) {
      throw new AirlineRequestErrorException();
    }
  }

  public String getReaderBeanName() {
    return readerBeanName;
  }
}

