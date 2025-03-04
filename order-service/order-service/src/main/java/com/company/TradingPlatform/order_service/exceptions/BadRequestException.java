package com.company.TradingPlatform.order_service.exceptions;

public class BadRequestException extends RuntimeException{

  public BadRequestException(String message){
      super(message);
  }
}
