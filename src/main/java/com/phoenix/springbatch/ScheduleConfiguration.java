package com.phoenix.springbatch;

import java.util.Date;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class ScheduleConfiguration {


  private final JobLauncher jobLauncher;

  private final Job job;

  public ScheduleConfiguration(JobLauncher jobLauncher, Job job) {
    this.jobLauncher = jobLauncher;
    this.job = job;
  }


  @Scheduled(cron = "0/30 * * * * *")
  public void runJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, Exception {
    JobParametersBuilder paramBuilder = new JobParametersBuilder();
    paramBuilder.addDate("runTime", new Date());
    this.jobLauncher.run(job, paramBuilder.toJobParameters());
  }

}
