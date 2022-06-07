package com.phoenix.springbatch.service.model;

public class ErrorDto {
  private String message;


  public ErrorDto(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
