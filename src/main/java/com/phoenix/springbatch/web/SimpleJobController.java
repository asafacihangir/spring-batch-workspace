package com.phoenix.springbatch.web;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class SimpleJobController {

  private final JobLauncher jobLauncher;

  private final Job job;

  public SimpleJobController(JobLauncher jobLauncher,
      @Qualifier("importCustomersJob") Job job) {
    this.jobLauncher = jobLauncher;
    this.job = job;
  }


  @PostMapping("/importCustomers")
  public void importCsvToDBJob() {
    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("startAt", System.currentTimeMillis()).toJobParameters();
    try {
      jobLauncher.run(job, jobParameters);
    } catch (JobExecutionAlreadyRunningException | JobRestartException |
             JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
}
