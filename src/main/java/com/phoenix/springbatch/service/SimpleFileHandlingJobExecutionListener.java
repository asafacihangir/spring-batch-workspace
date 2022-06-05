package com.phoenix.springbatch.service;

import com.phoenix.springbatch.config.AnonymizeJobParameterKeys;
import com.phoenix.springbatch.service.base.FileHandlingJobExecutionListener;
import com.phoenix.springbatch.utils.FileOperationUtils;
import java.io.File;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;

@Component
public class SimpleFileHandlingJobExecutionListener implements FileHandlingJobExecutionListener {



  @Override
  public void beforeJob(JobExecution jobExecution) {
    JobParameters jobParameters = jobExecution.getJobParameters();
    String uploadedFile = jobParameters.getString(AnonymizeJobParameterKeys.UPLOAD_PATH);
    String inputFile = jobParameters.getString(AnonymizeJobParameterKeys.INPUT_PATH);
    FileOperationUtils.moveFileToDirectory(new File(uploadedFile), new File(inputFile).getParent());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    JobParameters jobParameters = jobExecution.getJobParameters();
    String inputFile = jobParameters.getString(AnonymizeJobParameterKeys.INPUT_PATH);

    if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
      FileOperationUtils.deleteFile(inputFile);
    } else {
      String errorFile = jobParameters.getString(AnonymizeJobParameterKeys.ERROR_PATH);
      FileOperationUtils.moveFileToDirectory(new File(inputFile), new File(errorFile).getParent());
    }
  }

}
