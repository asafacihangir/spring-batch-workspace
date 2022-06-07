package com.phoenix.springbatch.job;

import com.phoenix.springbatch.service.AirlineSearchService;
import com.phoenix.springbatch.service.enums.Airport;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.Set;

public class ProviderDecider implements JobExecutionDecider {

  @Override
  public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
    JobParameters jobParameters = jobExecution.getJobParameters();

    Airport departure = findAirport(jobParameters, AirlineSearchService.JOB_KEY_DEPARTURE_AIRPORT);
    Airport arrival = findAirport(jobParameters, AirlineSearchService.JOB_KEY_ARRIVAL_AIRPORT);

    Set<Airport> flightAirports = Set.of(departure, arrival);
    if (flightAirports.contains(Airport.DUBAI) && flightAirports.contains(Airport.AMSTERDAM)) {
      return new FlowExecutionStatus("DUBAI_AMSTERDAM_OFFER_ONLY");
    } else {
      return new FlowExecutionStatus("DEFAULT_SEARCH");
    }
  }

  private Airport findAirport(JobParameters jobParameters, String paramName){
    String paramValue = jobParameters.getString(paramName);

    return Airport.valueOf(paramValue);
  }

}

