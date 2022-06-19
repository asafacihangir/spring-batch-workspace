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
  private final Job job2;

  public JobListController(JobLauncher jobLauncher, @Qualifier("job1") Job job,
      @Qualifier("job2") Job job2) {
    this.jobLauncher = jobLauncher;
    this.job = job;
    this.job2 = job2;
  }

  @PostMapping("/run-multithread-job")
  public void runMultithreadedJob()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    JobParameters jobParameter = new JobParametersBuilder().toJobParameters();

    jobLauncher.run(job, jobParameter);
  }

  @PostMapping("/run-async-job")
  public void run()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    JobParameters jobParameter = new JobParametersBuilder().toJobParameters();

    jobLauncher.run(job2, jobParameter);
  }






}