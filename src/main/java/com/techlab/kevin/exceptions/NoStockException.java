package com.techlab.kevin.exceptions;


public class NoStockException extends RuntimeException {

  public NoStockException(String message) {
    super(message);
  }
}
