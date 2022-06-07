package com.phoenix.springbatch.web;

import com.phoenix.springbatch.service.model.ErrorDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CommonExceptionHandler
{
  private static final Logger LOGGER = LogManager.getLogger();

  @ExceptionHandler(NoHandlerFoundException.class)
  public ErrorDto handleError404(HttpServletRequest request, Exception e)   {
    LOGGER.warn("Request: " + request.getRequestURL() + " raised " + e);
    return new ErrorDto("404 - not found or all necessary parameters given.");
  }
}