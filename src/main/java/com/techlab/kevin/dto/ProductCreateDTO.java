package com.techlab.kevin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductCreateDTO {

  @NotBlank
  private String name;

  @NotNull
  private Double price;

  @Min(0)
  private Integer stock;
}