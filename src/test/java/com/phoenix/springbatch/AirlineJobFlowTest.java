package com.phoenix.springbatch;

import static org.assertj.core.api.Assertions.assertThat;

import com.phoenix.springbatch.job.AirlineConfiguration;
import com.phoenix.springbatch.job.JobConfiguration;
import com.phoenix.springbatch.service.AirlineSearchService;
import com.phoenix.springbatch.service.enums.Airline;
import com.phoenix.springbatch.service.enums.Airport;
import com.phoenix.springbatch.service.model.SimulatorResponseDto;
import com.phoenix.springbatch.utils.DateUtils;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = {AirlineJobFlowTest.TestConfig.class, JobConfiguration.class})
@EnableBatchProcessing
class AirlineJobFlowTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @MockBean(name = Airline.TRANS_AMERICAN_READER)
  private ItemReader<SimulatorResponseDto> transAmericanReader;

  @MockBean(name = Airline.ADIOS_READER)
  private ItemReader<SimulatorResponseDto> adiosAirlineItemReader;

  @MockBean(name = Airline.OCEANIC_AIRLINE_READER)
  private ItemReader<SimulatorResponseDto> oceanicAirlineItemReader;

  @MockBean(name = Airline.BELARUS_AIRLINE_READER)
  private ItemReader<SimulatorResponseDto> belarusAirlineItemReader;

  @MockBean(name = Airline.FLY_US_READER)
  private ItemReader<SimulatorResponseDto> flyUsAirlineItemReader;

  @MockBean(name = Airline.SOUTH_PACIFIC_READER)
  private ItemReader<SimulatorResponseDto> southPacificAirlineItemReader;

  @MockBean(name = AirlineConfiguration.DUBAI_AMSTERDAM_OFFER_TASKLET)
  public Tasklet saveDubaiAmsterdamOffer;

  @MockBean(name = AirlineConfiguration.NEW_YORK_AMSTERDAM_OFFER_TASKLET)
  public Tasklet saveNewYorkAmsterdamOffer;

  @Test
  void thatMultipleAirlinesAreRequested() throws Exception {
    // given
    Airport departureAirport = Airport.PARIS;
    Airport arrivalAirport = Airport.LONDON;
    JobParameters jobParameters = createJobParameters(departureAirport, arrivalAirport);

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // then
    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    Mockito.verify(adiosAirlineItemReader).read();
    Mockito.verify(belarusAirlineItemReader).read();
  }

  @Test
  void thatDubaiAmsterdamSpecialOfferIsUsed() throws Exception {
    // given
    Airport departureAirport = Airport.DUBAI;
    Airport arrivalAirport = Airport.AMSTERDAM;
    JobParameters jobParameters = createJobParameters(departureAirport, arrivalAirport);

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // then
    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    Mockito.verify(saveDubaiAmsterdamOffer).execute(
        Mockito.any(StepContribution.class),
        Mockito.any(ChunkContext.class));
    Mockito.verify(adiosAirlineItemReader, Mockito.never()).read();
    Mockito.verify(belarusAirlineItemReader, Mockito.never()).read();
  }

  @Test
  void thatNewYorkAmsterdamSpecialOfferIsUsed() throws Exception {
    // TODO add test logic here
  }

  @Test
  void thatTimeoutIsSkipped() throws Exception {
    // TODO add test logic here
  }

  @Configuration
  static class TestConfig {

    @Autowired
    private Job airlineSearchJob;

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
      JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
      jobLauncherTestUtils.setJob(airlineSearchJob);
      return jobLauncherTestUtils;
    }

  }

  private JobParameters createJobParameters(Airport departureAirport, Airport arrivalAirport) {
    return new JobParametersBuilder()
        .addParameter(AirlineSearchService.JOB_KEY_SEARCH_ID, new JobParameter("-"))
        .addParameter(AirlineSearchService.JOB_KEY_DEPARTURE_DATE,
            new JobParameter(DateUtils.toDate(LocalDate.now())))
        .addParameter(AirlineSearchService.JOB_KEY_DEPARTURE_AIRPORT,
            new JobParameter(departureAirport.name()))
        .addParameter(AirlineSearchService.JOB_KEY_ARRIVAL_AIRPORT,
            new JobParameter(arrivalAirport.name()))
        .toJobParameters();
  }
}
