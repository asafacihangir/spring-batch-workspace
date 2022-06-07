package com.phoenix.springbatch.job;

import com.phoenix.springbatch.service.enums.Airline;
import com.phoenix.springbatch.service.model.SimulatorResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemReaderException;

import java.time.LocalDate;

public class AirlineItemReader implements ItemReader<SimulatorResponseDto> {

  private static final Logger LOGGER = LogManager.getLogger(AirlineItemReader.class);
  private final Airline airline;
  private final AirlineResponseSimulator simulator;
  private boolean itemsRead = false;

  public AirlineItemReader(Airline airline, AirlineResponseSimulator simulator) {
    this.airline = airline;
    this.simulator = simulator;
  }

  @Override
  public SimulatorResponseDto read()
      throws ItemReaderException {
    if (itemsRead) {
      return null;
    } else {
      LOGGER.info("Load result for {}",airline);
      this.itemsRead = true;
      return simulator.getOffer(airline, LocalDate.now());
    }
  }
}
