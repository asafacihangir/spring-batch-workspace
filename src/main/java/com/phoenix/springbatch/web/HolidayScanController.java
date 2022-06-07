package com.phoenix.springbatch.web;

import com.phoenix.springbatch.repository.FlightSearchResultRepository;
import com.phoenix.springbatch.service.AirlineSearchService;
import com.phoenix.springbatch.service.model.FlightOfferDto;
import com.phoenix.springbatch.service.model.SearchDto;
import java.util.List;
import java.util.UUID;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/holiday")
public class HolidayScanController {

  private final AirlineSearchService airlineSearchService;
  private final FlightSearchResultRepository flightSearchResultRepository;

  public HolidayScanController(AirlineSearchService airlineSearchService,
      FlightSearchResultRepository flightSearchResultRepository) {
    this.airlineSearchService = airlineSearchService;
    this.flightSearchResultRepository = flightSearchResultRepository;
  }

  @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
  public List<FlightOfferDto> getOffers(@RequestBody @Validated SearchDto searchDto)
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
    String searchId = UUID.randomUUID().toString();
    airlineSearchService.search(searchId, searchDto.flightDate, searchDto.departureAirport,
        searchDto.arrivalAirport);
    return flightSearchResultRepository.findResults(searchId);
  }
}
