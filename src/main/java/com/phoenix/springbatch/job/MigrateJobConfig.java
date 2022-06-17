package com.phoenix.springbatch.job;

import com.phoenix.springbatch.mysql.domain.MysqlStudent;
import com.phoenix.springbatch.postgres.domain.PostgresStudent;
import javax.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.HibernatePagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MigrateJobConfig {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  private final FirstItemProcessor firstItemProcessor;

  private final PlatformTransactionManager jpaTransactionManager;

  private final EntityManagerFactory postgresEntityManager;

  private final EntityManagerFactory mysqlEntityManagerFactory;


  public MigrateJobConfig(JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      FirstItemProcessor firstItemProcessor,
      @Qualifier("mysqlTransactionManager") PlatformTransactionManager jpaTransactionManager,
      @Qualifier("postgresEntityManagerFactory") EntityManagerFactory postgresEntityManager,
      @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory mysqlEntityManagerFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.firstItemProcessor = firstItemProcessor;
    this.jpaTransactionManager = jpaTransactionManager;
    this.postgresEntityManager = postgresEntityManager;
    this.mysqlEntityManagerFactory = mysqlEntityManagerFactory;
  }

  @Bean
  public Job chunkJob() {
    return jobBuilderFactory.get("Chunk Job")
        .incrementer(new RunIdIncrementer())
        .start(firstChunkStep())
        .build();
  }

  private Step firstChunkStep() {
    return stepBuilderFactory.get("First Chunk Step")
        .<PostgresStudent, MysqlStudent>chunk(100)
        .reader(customerItemReader())
        .processor(firstItemProcessor)
        .writer(jpaItemWriter())
        .faultTolerant()
        .skip(Throwable.class)
        .skipLimit(100)
        .retryLimit(3)
        .retry(Throwable.class)
        .transactionManager(jpaTransactionManager)
        .taskExecutor(asyncTaskExecutor())
        .build();
  }


  public JpaItemWriter<MysqlStudent> jpaItemWriter() {
    JpaItemWriter<MysqlStudent> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(mysqlEntityManagerFactory);
    return jpaItemWriter;
  }

  @Bean
  @StepScope
  public HibernatePagingItemReader<PostgresStudent> customerItemReader() {
    return new HibernatePagingItemReaderBuilder<PostgresStudent>()
        .name("customerItemReader")
        .sessionFactory(postgresEntityManager.unwrap(SessionFactory.class))
        .queryString("from PostgresStudent")
        .pageSize(10)
        .build();
  }

  @Bean
  public TaskExecutor asyncTaskExecutor() {
    SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
    taskExecutor.setConcurrencyLimit(10);
    return taskExecutor;
  }

}
