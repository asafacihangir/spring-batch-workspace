package com.phoenix.springbatch.job;

import com.phoenix.springbatch.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
public class JobConfig {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;
  private final CustomerWriter customerWriter;

  private final CustomerProcessor customerProcessor;

  private final ColumnRangePartitioner columnRangePartitioner;

  public JobConfig(JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      CustomerWriter customerWriter,
      CustomerProcessor customerProcessor,
      ColumnRangePartitioner columnRangePartitioner) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.customerWriter = customerWriter;
    this.customerProcessor = customerProcessor;
    this.columnRangePartitioner = columnRangePartitioner;
  }

  @Bean
  public FlatFileItemReader<Customer> reader() {
    FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
    itemReader.setResource(new FileSystemResource("./data/customers.csv"));
    itemReader.setName("csvReader");
    itemReader.setLinesToSkip(1);
    itemReader.setLineMapper(lineMapper());
    return itemReader;
  }

  private LineMapper<Customer> lineMapper() {
    DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(",");
    lineTokenizer.setStrict(false);
    lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country",
        "dob");

    BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(Customer.class);

    lineMapper.setLineTokenizer(lineTokenizer);
    lineMapper.setFieldSetMapper(fieldSetMapper);
    return lineMapper;
  }

  @Bean
  public PartitionHandler partitionHandler() {
    TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
    taskExecutorPartitionHandler.setGridSize(4);
    taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
    taskExecutorPartitionHandler.setStep(slaveStep());
    return taskExecutorPartitionHandler;
  }

  @Bean
  public Step slaveStep() {
    return stepBuilderFactory.get("slaveStep").<Customer, Customer>chunk(250)
        .reader(reader())
        .processor(customerProcessor)
        .writer(customerWriter)
        .build();
  }


  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setMaxPoolSize(4);
    taskExecutor.setCorePoolSize(4);
    taskExecutor.setQueueCapacity(4);
    return taskExecutor;
  }

  @Bean
  public Step masterStep() {
    return stepBuilderFactory.get("masterSTep").
        partitioner(slaveStep().getName(), columnRangePartitioner)
        .partitionHandler(partitionHandler())
        .build();
  }


  @Bean
  public Job importCustomersJob() {
    return jobBuilderFactory.get("importCustomers")
        .flow(masterStep())
        .end().build();

  }


}
