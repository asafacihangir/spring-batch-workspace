package com.phoenix.springbatch;

import java.time.LocalDateTime;
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
@RequestMapping("/api/job-list")
public class JobListController {

  private final JobLauncher jobLauncher;

  private final Job deliverPackageJob;

  private final Job prepareFlowersJob;

  public JobListController(JobLauncher jobLauncher,
      @Qualifier("deliverPackageJob") Job deliverPackageJob,
      @Qualifier("prepareFlowers")Job prepareFlowersJob) {
    this.jobLauncher = jobLauncher;
    this.deliverPackageJob = deliverPackageJob;
    this.prepareFlowersJob = prepareFlowersJob;
  }

  @PostMapping("/run-delivery-package")
  public void runDeliverPackageJob()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    JobParameters jobParameter = new JobParametersBuilder()
        .addString("item", "shoes")
        .addString("type", "roses")
        .addString("run.date", LocalDateTime.now().toString()).toJobParameters();

    jobLauncher.run(deliverPackageJob, jobParameter);
  }

  @PostMapping("/run-flower-package")
  public void runPrepareFlowers()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    JobParameters jobParameter = new JobParametersBuilder()
        .addString("run.date", LocalDateTime.now().toString())
        .addString("type", "roses")
        .toJobParameters();

    jobLauncher.run(prepareFlowersJob, jobParameter);
  }


}
