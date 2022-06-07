package com.phoenix.springbatch.service;

import com.phoenix.springbatch.service.enums.Airport;
import com.phoenix.springbatch.utils.DateUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AirlineSearchService {

  public static final String JOB_KEY_SEARCH_ID = "searchId";
  public static final String JOB_KEY_DEPARTURE_DATE = "departureDate";
  public static final String JOB_KEY_DEPARTURE_AIRPORT = "departureAirport";
  public static final String JOB_KEY_ARRIVAL_AIRPORT = "arrivalAirport";

  private final JobLauncher jobLauncher;
  private final Job job;

  public AirlineSearchService(JobLauncher jobLauncher, Job job) {
    this.jobLauncher = jobLauncher;
    this.job = job;
  }


  public void search(String searchId,
      LocalDate requestDate,
      Airport departureAirport,
      Airport arrivalAirport) throws JobParametersInvalidException,
      JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

    JobParameters jobParameters = new JobParametersBuilder()
        .addParameter(JOB_KEY_SEARCH_ID, new JobParameter(searchId))
        .addParameter(JOB_KEY_DEPARTURE_DATE, new JobParameter(DateUtils.toDate(requestDate)))
        .addParameter(JOB_KEY_DEPARTURE_AIRPORT, new JobParameter(departureAirport.name()))
        .addParameter(JOB_KEY_ARRIVAL_AIRPORT, new JobParameter(arrivalAirport.name()))
        .toJobParameters();
    jobLauncher.run(job, jobParameters);
  }
}
