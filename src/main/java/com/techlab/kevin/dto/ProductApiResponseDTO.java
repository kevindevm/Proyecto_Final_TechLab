package com.techlab.kevin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductApiResponseDTO<Product> {

  private String message;
  private Integer id;
  private Product product;
  private List<Product> results;
  private Integer foundProducts;
  private Integer totalProducts;
  private String timestamp;
  private String keyword;

  public ProductApiResponseDTO(String message) {
    this.message = message;
    this.timestamp = LocalDateTime.now().toString();
  }

  public ProductApiResponseDTO(String message, Integer id) {
    this.message = message;
    this.id = id;
    this.timestamp = LocalDateTime.now().toString();
  }


  public ProductApiResponseDTO(String message, Product product) {
    this.message = message;
    this.product = product;
    this.timestamp = LocalDateTime.now().toString();
  }

  public ProductApiResponseDTO(String keyword, List<Product> results, int total) {
    this.keyword = keyword;
    this.results = results;
    this.foundProducts = results.size();
    this.totalProducts = total;
    this.timestamp = LocalDateTime.now().toString();
  }
}
