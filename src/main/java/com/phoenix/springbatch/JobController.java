package com.phoenix.springbatch;

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
@RequestMapping("/api/job")
public class JobController {

  private final Job job;

  private final JobLauncher jobLauncher;


  public JobController(@Qualifier("readShippedOrdersJob") Job job, JobLauncher jobLauncher) {
    this.job = job;
    this.jobLauncher = jobLauncher;
  }

  @PostMapping("/run-read-shipped-orders-job")
  public void runReadShippedOrdersJob()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

    JobParameters jobParameter = new JobParametersBuilder()
        .toJobParameters();
    jobLauncher.run(job, jobParameter);

  }

}
