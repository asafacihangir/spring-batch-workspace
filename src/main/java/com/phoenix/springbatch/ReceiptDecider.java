package com.phoenix.springbatch;

import java.util.Random;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class ReceiptDecider implements JobExecutionDecider {

  public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
    String exitCode = new Random().nextFloat() < .70f ? "CORRECT" : "INCORRECT";
    System.out.println("The item delivered is: " + exitCode);
    return new FlowExecutionStatus(exitCode);
  }
}
