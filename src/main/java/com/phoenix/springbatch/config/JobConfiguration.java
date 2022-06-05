package com.phoenix.springbatch.config;

import com.phoenix.springbatch.service.base.FileHandlingJobExecutionListener;
import com.phoenix.springbatch.service.model.Person;
import com.phoenix.springbatch.utils.FileOperationUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class JobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  private final FileHandlingJobExecutionListener fileHandlingJobExecutionListener;


  public JobConfiguration(JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      FileHandlingJobExecutionListener fileHandlingJobExecutionListener) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.fileHandlingJobExecutionListener = fileHandlingJobExecutionListener;
  }

  @Bean
  public Job job() {
    return jobBuilderFactory.get("anonymizeJob")
        .start(step())
        .listener(fileHandlingJobExecutionListener)
        .validator(new AnonymizeJobParameterValidator())
        .build();
  }

  @Bean
  public Step step() {
    SimpleStepBuilder<Person, Person> simpleStepBuilder = stepBuilderFactory.get("anonymizeStep")
        .chunk(1);
    return simpleStepBuilder.reader(reader(null))
        .processor(processor())
        .writer(writer(null))
        .build();
  }


  @Bean
  @StepScope
  public JsonItemReader<Person> reader(
      @Value(AnonymizeJobParameterKeys.INPUT_PATH_REFERENCE) String inputPath) {
    FileSystemResource resource = FileOperationUtils.getFileResource(inputPath);

    return new JsonItemReaderBuilder<Person>()
        .name("jsonItemReader")
        .resource(resource)
        .jsonObjectReader(new JacksonJsonObjectReader<>(Person.class))
        .build();
  }


  @Bean
  public ItemProcessor<Person, Person> processor() {
    return person -> {
      if (!person.getCustomer()) {
        return null;
      }

      Person output = new Person();
      output.setBirthday(person.getBirthday());
      output.setEmail(person.getEmail());
      output.setCustomer(person.getCustomer());
      output.setName(person.getName());
      output.setRevenue(person.getRevenue());
      return output;
    };
  }


  @Bean
  @StepScope
  public JsonFileItemWriter<Person> writer(@Value(AnonymizeJobParameterKeys.OUTPUT_PATH_REFERENCE) String outputPath) {
    FileSystemResource resource = FileOperationUtils.getFileResource(outputPath);

    return new JsonFileItemWriterBuilder<Person>()
        .name("jsonItemWriter")
        .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
        .resource(resource)
        .build();
  }


}
