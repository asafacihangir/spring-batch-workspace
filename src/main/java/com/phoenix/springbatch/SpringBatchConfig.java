package com.phoenix.springbatch;

import java.time.LocalDateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class SpringBatchConfig {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Autowired
  public JobLauncher jobLauncher;

  @Bean
  public Step step() throws Exception {
    return this.stepBuilderFactory.get("step").tasklet((contribution, chunkContext) -> {
      System.out.println("The run time is: " + LocalDateTime.now());
      return RepeatStatus.FINISHED;
    }).build();
  }

  @Bean
  public Job job() throws Exception {
    return this.jobBuilderFactory.get("job").start(step()).build();
  }


}
