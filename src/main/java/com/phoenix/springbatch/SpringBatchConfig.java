package com.phoenix.springbatch;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
  public static String[] names = new String[] { "orderId", "firstName", "lastName", "email", "cost", "itemId",
      "itemName", "shipDate" };

  public static String ORDER_SQL = "select order_id, first_name, last_name, email, cost, item_id, item_name, ship_date "
      + "from SHIPPED_ORDER order by order_id";

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Autowired
  public DataSource dataSource;

  @Bean
  public ItemWriter<Order> itemWriter() {
    FlatFileItemWriter<Order> itemWriter = new FlatFileItemWriter<Order>();

    itemWriter.setResource(new FileSystemResource("./data/shipped_orders_output.csv"));

    DelimitedLineAggregator<Order> aggregator = new DelimitedLineAggregator<Order>();
    aggregator.setDelimiter(",");

    BeanWrapperFieldExtractor<Order> fieldExtractor = new BeanWrapperFieldExtractor<Order>();
    fieldExtractor.setNames(names);
    aggregator.setFieldExtractor(fieldExtractor);

    itemWriter.setLineAggregator(aggregator);
    return itemWriter;
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
        .build();

  }

  @Bean
  public Step chunkBasedStep() throws Exception {
    return this.stepBuilderFactory.get("chunkBasedStep").<Order, Order>chunk(10).reader(itemReader())
        .writer(itemWriter()).build();
  }

  @Bean
  public Job job() throws Exception {
    return this.jobBuilderFactory.get("job").start(chunkBasedStep()).build();
  }


}
