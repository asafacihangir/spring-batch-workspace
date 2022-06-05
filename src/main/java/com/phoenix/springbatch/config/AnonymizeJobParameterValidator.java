package com.phoenix.springbatch.config;

import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.DefaultJobParametersValidator;

public class AnonymizeJobParameterValidator  extends DefaultJobParametersValidator {

  private static final String[] REQUIRED_KEYS = {
      AnonymizeJobParameterKeys.INPUT_PATH,
      AnonymizeJobParameterKeys.OUTPUT_PATH
  };

  private static final String[] OPTIONAL_KEYS = {};

  public AnonymizeJobParameterValidator() {
    super(REQUIRED_KEYS, OPTIONAL_KEYS);
  }

  @Override
  public void validate(JobParameters parameters) throws JobParametersInvalidException {
    super.validate(parameters);

    String inputPath = parameters.getString(AnonymizeJobParameterKeys.INPUT_PATH);
    String extension = FilenameUtils.getExtension(inputPath);
    if (extension == null || !extension.equals("json")) {
      throw new JobParametersInvalidException("Input file must be in JSON Format");
    }
  }

}
