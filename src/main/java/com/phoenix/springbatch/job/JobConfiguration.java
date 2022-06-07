package com.phoenix.springbatch.job;

import com.phoenix.springbatch.service.enums.Airline;
import com.phoenix.springbatch.service.model.SimulatorResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

  private static final Logger LOGGER = LogManager.getLogger(JobConfiguration.class);

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final ApplicationContext applicationContext;


  public JobConfiguration(JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory, ApplicationContext applicationContext) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.applicationContext = applicationContext;
  }


  @Bean
  public Job job() throws Exception {
    return jobBuilderFactory.get("airlineSearchJob")
        .start(searchFlow())
        .end()
        .listener(new JobSummaryListener())
        .build();
  }

  @Bean
  public Flow searchFlow() throws Exception {
    ProviderDecider decider = new ProviderDecider();
    return new FlowBuilder<SimpleFlow>("searchFlow")
        .start(decider)
        .on("DUBAI_AMSTERDAM_OFFER_ONLY")
        .to(dubaiAmsterdamOffer())
        .from(decider)
        .on("DEFAULT_SEARCH")
        .to(requestAdiosStep())
        .next(requestBelarusStep())
        .build();
  }


  @Bean
  public Step requestOceanicStep() {
    return requestAirlineReaderStep(Airline.OCEANIC);
  }

  @Bean
  public Step requestBelarusStep() {
    return requestAirlineReaderStep(Airline.BELARUS);
  }

  @Bean(value = "requestTransamericanStep")
  public Step requestTransamericanStep() {
    return requestAirlineReaderStep(Airline.TRANS_AMERICAN);
  }

  @Bean
  public Step requestSouthPacificStep() {
    return requestAirlineReaderStep(Airline.SOUTH_PACIFIC);
  }

  @Bean
  public Step requestAdiosStep() {
    return requestAirlineReaderStep(Airline.ADIOS);
  }

  @Bean
  public Step requestFlyUsStep() {
    return requestAirlineReaderStep(Airline.FLY_US);
  }

  @SuppressWarnings("unchecked")
  private Step requestAirlineReaderStep(Airline airline) {
    String beanName = airline.getReaderBeanName();
    ItemReader<SimulatorResponseDto> reader = applicationContext.getBean(beanName, ItemReader.class);
    return stepBuilderFactory.get("request_" + airline)
        .<SimulatorResponseDto, SimulatorResponseDto>chunk(1)
        .reader(reader)
        .writer(airlineSearchResultWriter())
        .build();
  }

  @Bean
  @StepScope
  public AirlineSearchResultWriter airlineSearchResultWriter() {
    return new AirlineSearchResultWriter();
  }

  @Bean
  public Step newYorkAmsterdamOffer() {
    return stepBuilderFactory.get("newYorkAmsterdamOffer")
        .tasklet(applicationContext.getBean(
            AirlineConfiguration.NEW_YORK_AMSTERDAM_OFFER_TASKLET, Tasklet.class
        )).build();
  }

  @Bean
  public Step dubaiAmsterdamOffer() {
    return stepBuilderFactory.get("dubaiAmsterdamOffer")
        .tasklet(applicationContext.getBean(
            AirlineConfiguration.DUBAI_AMSTERDAM_OFFER_TASKLET, Tasklet.class
        )).build();
  }
}
