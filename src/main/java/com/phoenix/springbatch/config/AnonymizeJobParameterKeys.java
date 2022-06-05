package com.phoenix.springbatch.config;

public interface AnonymizeJobParameterKeys {

  String INPUT_PATH = "inputPath";
  String OUTPUT_PATH = "outputPath";
  String UPLOAD_PATH = "uploadPath";
  String ERROR_PATH = "errorPath";

  String INPUT_PATH_REFERENCE = "#{jobParameters['"+INPUT_PATH+"']}";
  String OUTPUT_PATH_REFERENCE = "#{jobParameters['"+OUTPUT_PATH+"']}";
}