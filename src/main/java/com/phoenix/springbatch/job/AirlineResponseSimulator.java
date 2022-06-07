package com.phoenix.springbatch.job;


import com.phoenix.springbatch.exception.AirlineRequestErrorException;
import com.phoenix.springbatch.exception.AirlineRequestTimeoutException;
import com.phoenix.springbatch.service.enums.Airline;
import com.phoenix.springbatch.service.model.SimulatorFlightDto;
import com.phoenix.springbatch.service.model.SimulatorResponseDto;
import com.phoenix.springbatch.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;

@Component
public class AirlineResponseSimulator {

  private static final Logger LOGGER = LogManager.getLogger(AirlineResponseSimulator.class);
  private static final int TIMEOUT_THRESHOLD = 4000;

  private final Random random = new Random();

  public SimulatorResponseDto getOffer(Airline airline, Date departureDate) {
    return getOffer(airline,
        DateUtils.toLocalDate(departureDate));
  }

  public SimulatorResponseDto getOffer(Airline airline, LocalDate requestDate) throws AirlineRequestErrorException {
    airline.simulateError();

    LOGGER.info("Simulate request to {} ",airline);
    SimulatorResponseDto responseDto = new SimulatorResponseDto();
    simulateError(airline);

    int randomDelay = airline.getRandomDelay();
    if (randomDelay > TIMEOUT_THRESHOLD) {
      simulateDelay(TIMEOUT_THRESHOLD);
      LOGGER.info("Request to {} timed out", airline);
      throw new AirlineRequestTimeoutException();
    } else {
      simulateDelay(randomDelay);
    }

    for (int i = 0; i<airline.getNrOfOffers() + randomOfTen(); i++) {
      SimulatorFlightDto flightDto = new SimulatorFlightDto();
      LocalDateTime startTime = LocalDateTime.of(requestDate, LocalTime.MIDNIGHT)
          .plusHours(randomOfTen());
      flightDto.departureTime = startTime;
      flightDto.arrivalTime = startTime.plusHours(randomOfTen() + 4);
      flightDto.price = airline.getRandomPrice();
      responseDto.flights.add(flightDto);
    }
    responseDto.airlineName = airline.getAirlineName();
    LOGGER.info("Simulation for {} is finished",airline);
    return responseDto;
  }

  private void simulateError(Airline airline) {
    if (airline.getFailureRateInPercent() == 0){
      return;
    }
    int randomErrorPercentage = random.nextInt(100);
    if (randomErrorPercentage < airline.getFailureRateInPercent()) {
      throw new AirlineRequestErrorException();
    }
  }

  private long randomOfTen() {
    return random.nextInt(10);
  }

  private void simulateDelay(int delay) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

}
