package com.phoenix.springbatch.web;

import com.phoenix.springbatch.postgres.PostgresStudentService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class SimpleStudentController {

  private final PostgresStudentService postgresStudentService;

  private final JobLauncher jobLauncher;

  private final Job job;



  public SimpleStudentController(PostgresStudentService postgresStudentService,
      JobLauncher jobLauncher,
      @Qualifier("chunkJob") Job job) {
    this.postgresStudentService = postgresStudentService;
    this.jobLauncher = jobLauncher;
    this.job = job;
  }


  @GetMapping("/create-data")
  public void createStudentData() {
    postgresStudentService.createStudentData();
  }


  @PostMapping("/run-migrate-job")
  public void run()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    JobParameters jobParameter = new JobParametersBuilder().toJobParameters();

    jobLauncher.run(job, jobParameter);
  }


}
