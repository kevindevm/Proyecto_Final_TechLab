package com.techlab.kevin.exceptions;

public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(String message) {
    super(message);
  }
}
