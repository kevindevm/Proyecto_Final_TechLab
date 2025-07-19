package com.techlab.kevin.dto;


import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OrderItemQuantityUpdateDTO {

  @Min(value = 1, message = "Quantity must be at least 1")
  private Integer quantity;
}
