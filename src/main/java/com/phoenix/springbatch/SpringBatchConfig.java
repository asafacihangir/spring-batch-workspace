package com.phoenix.springbatch;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

  public static String INSERT_ORDER_SQL = "insert into "
      + "TRACKED_ORDER(order_id, first_name, last_name, email, item_id, item_name, cost, ship_date, tracking_number, free_shipping)"
      + " values(:orderId,:firstName,:lastName,:email,:itemId,:itemName,:cost,:shipDate,:trackingNumber, :freeShipping)";

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Autowired
  public DataSource dataSource;

  @Bean
  public ItemProcessor<TrackedOrder, TrackedOrder> freeShippingItemProcessor() {
    return new FreeShippingItemProcessor();
  }

  @Bean
  public ItemProcessor<Order, TrackedOrder> compositeItemProcessor() {
    return new CompositeItemProcessorBuilder<Order,TrackedOrder>()
        .delegates(orderValidatingItemProcessor(), trackedOrderItemProcessor(), freeShippingItemProcessor())
        .build();
  }

  @Bean
  public ItemProcessor<Order, TrackedOrder> trackedOrderItemProcessor() {
    return new TrackedOrderItemProcessor();
  }

  @Bean
  public ItemProcessor<Order, Order> orderValidatingItemProcessor() {
    BeanValidatingItemProcessor<Order> itemProcessor = new BeanValidatingItemProcessor<Order>();
    itemProcessor.setFilter(true);
    return itemProcessor;
  }


  @Bean
  public ItemWriter<TrackedOrder> itemWriter () {
    return new JdbcBatchItemWriterBuilder<TrackedOrder>().dataSource(dataSource)
        .sql(INSERT_ORDER_SQL)
        .beanMapped()
        .build();
  }


  @Bean
  public PagingQueryProvider queryProvider() throws Exception {
    SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();

    factory.setSelectClause("select order_id, first_name, last_name, email, cost, item_id, item_name, ship_date");
    factory.setFromClause("from SHIPPED_ORDER");
    factory.setSortKey("order_id");
    factory.setDataSource(dataSource);
    return factory.getObject();
  }

  @Bean
  public ItemReader<Order> itemReader() throws Exception {
    return new JdbcPagingItemReaderBuilder<Order>()
        .dataSource(dataSource)
        .name("jdbcCursorItemReader")
        .queryProvider(queryProvider())
        .rowMapper(new OrderRowMapper())
        .pageSize(10)
        .saveState(false)
        .build();

  }

  @Primary
  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(10);
    return executor;
  }

  @Bean
  public Step chunkBasedStep() throws Exception {
    return this.stepBuilderFactory.get("chunkBasedStep")
        .<Order, TrackedOrder>chunk(10)
        .reader(itemReader())
        .processor(compositeItemProcessor())
        .faultTolerant()
        .retry(OrderProcessingException.class)
        .retryLimit(3)
        .listener(new CustomRetryListener())
        .writer(itemWriter())
        .taskExecutor(taskExecutor())
        .build();
  }


  @Bean
  public Step multi_thread_step() throws Exception {
    return stepBuilderFactory.get("multi_thread_step")
        .<Order, TrackedOrder>chunk(10)
        .reader(itemReader())
        .processor(compositeItemProcessor())
        .faultTolerant()
        .retry(OrderProcessingException.class)
        .retryLimit(3)//.reader(serviceAdapter())
        .listener(new CustomRetryListener())
        .writer(itemWriter())
        .taskExecutor(taskExecutor())
        .build();
  }

  @Bean
  public Step step0(){
    return  stepBuilderFactory.get("step0")
        .tasklet(new ConsoleTasklet())
        .build();
  }



  @Bean
  public AsyncItemProcessor asyncItemProcessor(){
    AsyncItemProcessor processor = new AsyncItemProcessor();
    processor.setDelegate(compositeItemProcessor());
    processor.setTaskExecutor(new SimpleAsyncTaskExecutor());
    return processor;
  }

  @Bean
  public AsyncItemWriter asyncItemWriter(){
    AsyncItemWriter writer = new AsyncItemWriter();
    writer.setDelegate(itemWriter());
    return writer;
  }


  @Bean
  public Step async_step() throws Exception {
    return stepBuilderFactory.get("async_step")
        .<Order, TrackedOrder>chunk(10)
        .reader(itemReader())
        .processor(asyncItemProcessor())
        .faultTolerant()
        .retry(OrderProcessingException.class)
        .retryLimit(3)//.reader(serviceAdapter())
        .listener(new CustomRetryListener())
        .writer(asyncItemWriter())
        .taskExecutor(taskExecutor())
        .build();
  }


  @Bean
  public Job job() throws Exception {
    return this.jobBuilderFactory.get("job").start(chunkBasedStep()).build();
  }


  @Bean
  public Job job1() throws Exception {
    return jobBuilderFactory.get("job1")
        .incrementer(new RunIdIncrementer())
        .start(step0())
        .next(multi_thread_step())
        .build();
  }


  @Bean
  public Job job2() throws Exception {
    return jobBuilderFactory.get("job2")
        .incrementer(new RunIdIncrementer())
        .start(step0())
        .next(async_step())
        .build();
  }








}
