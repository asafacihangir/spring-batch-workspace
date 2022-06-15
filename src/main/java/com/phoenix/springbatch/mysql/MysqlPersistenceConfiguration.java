package com.phoenix.springbatch.mysql;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "mysqlEntityManagerFactory",
    transactionManagerRef = "mysqlTransactionManager")
public class MysqlPersistenceConfiguration {

  private final Environment env;

  public MysqlPersistenceConfiguration(Environment env) {
    this.env = env;
  }


  @Primary
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSourceProperties productDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Primary
  @Bean(name = "mysqlDataSource")
  public DataSource mysqlDataSource() {
    return productDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class)
        .build();

  }


  @Primary
  @Bean(name = "mysqlEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean eveverseEntityManagerFactory(
      EntityManagerFactoryBuilder builder, @Qualifier("mysqlDataSource") DataSource dataSource) {
    final LocalContainerEntityManagerFactoryBean emf = builder.dataSource(dataSource)
        .packages("com.phoenix.springbatch.mysql").persistenceUnit("pu-mysql").build();

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(Boolean.TRUE);
    vendorAdapter.setShowSql(Boolean.TRUE);
    vendorAdapter.setDatabase(Database.MYSQL);

    emf.setJpaVendorAdapter(vendorAdapter);
    emf.setJpaPropertyMap(getJpaProperties());

    return emf;
  }

  @Primary
  @Bean(name = "mysqlTransactionManager")
  public PlatformTransactionManager mysqlTransactionManager(
      @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }


  private HashMap<String, Object> getJpaProperties() {
    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.jdbc.batch_size", 300);
    properties.put("hibernate.order_inserts", true);
    properties.put("hibernate.order_updates", true);
    properties.put("hibernate.batch_versioned_data", true);
    properties.put("hibernate.cache.use_second_level_cache", false);
    properties.put("hibernate.connection.isolation",
        java.sql.Connection.TRANSACTION_READ_COMMITTED);

    properties.put("spring.jpa.hibernate.ddl-auto", "create");
    properties.put("hibernate.dialect", org.hibernate.dialect.MySQL8Dialect.class);

    properties.put("spring.jpa.open-in-view", false);
    properties.put("spring.jpa.hibernate.use-new-ID-generator-mappings", true);
    properties.put("spring.jpa.hibernate.naming.implicit-strategy",
        SpringImplicitNamingStrategy.INSTANCE);

    return properties;

  }


}