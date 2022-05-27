package com.phoenix.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchApplication {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Bean
  public JobExecutionDecider decider() {
    return new DeliveryDecider();
  }

  @Bean
  public JobExecutionDecider receiptDecider() {
    return new ReceiptDecider();
  }

  @Bean
  public Step thankCustomerStep() {
    return this.stepBuilderFactory.get("thankCustomerStep").tasklet(
        (contribution, chunkContext) -> {
          System.out.println("Thanking the customer.");
          return RepeatStatus.FINISHED;
        }).build();
  }

  @Bean
  public Step refundStep() {
    return this.stepBuilderFactory.get("refundStep").tasklet((contribution, chunkContext) -> {
      System.out.println("Refunding customer money.");
      return RepeatStatus.FINISHED;
    }).build();
  }

  @Bean
  public Step leaveAtDoorStep() {
    return this.stepBuilderFactory.get("leaveAtDoorStep").tasklet((contribution, chunkContext) -> {
      System.out.println("Leaving the package at the door.");
      return RepeatStatus.FINISHED;
    }).build();
  }

  @Bean
  public Step storePackageStep() {
    return this.stepBuilderFactory.get("storePackageStep").tasklet((contribution, chunkContext) -> {
      System.out.println("Storing the package while the customer address is located.");
      return RepeatStatus.FINISHED;
    }).build();
  }

  @Bean
  public Step givePackageToCustomerStep() {
    return this.stepBuilderFactory.get("givePackageToCustomer").tasklet(
        (contribution, chunkContext) -> {
          System.out.println("Given the package to the customer.");
          return RepeatStatus.FINISHED;
        }).build();
  }

  @Bean
  public Step driveToAddressStep() {

    boolean GOT_LOST = false;
    return this.stepBuilderFactory.get("driveToAddressStep").tasklet(
        (contribution, chunkContext) -> {

          if (GOT_LOST) {
            throw new RuntimeException("Got lost driving to the address");
          }

          System.out.println("Successfully arrived at the address.");
          return RepeatStatus.FINISHED;
        }).build();
  }

  @Bean
  public Step packageItemStep() {
    return this.stepBuilderFactory.get("packageItemStep").tasklet((contribution, chunkContext) -> {

      String item = "", date = "";
      if (chunkContext.getStepContext().getJobParameters().containsKey("item")) {
        item = chunkContext.getStepContext().getJobParameters().get("item").toString();
      }

      if (chunkContext.getStepContext().getJobParameters().containsKey("run.date")) {
        date = chunkContext.getStepContext().getJobParameters().get("run.date").toString();
      }
      System.out.printf("The %s has been packaged on %s.%n", item, date);
      return RepeatStatus.FINISHED;
    }).build();
  }

  @Bean
  public Job deliverPackageJob() {
    return this.jobBuilderFactory.get("deliverPackageJob")
        .start(packageItemStep())
        .next(driveToAddressStep())
        .on("FAILED").to(storePackageStep())
        .from(driveToAddressStep())
        .on("*").to(decider())
        .on("PRESENT").to(givePackageToCustomerStep())
        .next(receiptDecider()).on("CORRECT").to(thankCustomerStep())
        .from(receiptDecider()).on("INCORRECT").to(refundStep())
        .from(decider())
        .on("NOT_PRESENT").to(leaveAtDoorStep())
        .end()
        .build();
  }


  public static void main(String[] args) {
    SpringApplication.run(SpringBatchApplication.class, args);
  }

}
