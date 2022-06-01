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
@RequestMapping("/api/job-list")
public class JobListController {

  private final JobLauncher jobLauncher;

  private final Job job;

  public JobListController(JobLauncher jobLauncher, @Qualifier("job") Job job) {
    this.jobLauncher = jobLauncher;
    this.job = job;
  }

  @PostMapping("/run")
  public void run()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    JobParameters jobParameter = new JobParametersBuilder().toJobParameters();

    jobLauncher.run(job, jobParameter);
  }


}
