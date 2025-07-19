package com.techlab.kevin.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProductUpdateDTO {

  private String name;
  private Double price;
  @Min(0)
  private Integer stock;

}
