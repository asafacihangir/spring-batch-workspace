package com.phoenix.springbatch.service.model;

import java.util.ArrayList;
import java.util.List;

public class SimulatorResponseDto {
  public List<SimulatorFlightDto> flights = new ArrayList<>();
  public String airlineName;
}
