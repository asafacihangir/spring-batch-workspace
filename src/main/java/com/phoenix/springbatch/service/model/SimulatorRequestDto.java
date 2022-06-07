package com.phoenix.springbatch.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class SimulatorRequestDto {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate date;

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

}
