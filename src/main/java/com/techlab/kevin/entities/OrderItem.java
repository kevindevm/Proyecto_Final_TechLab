package com.techlab.kevin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor

public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  @JsonIgnore
  private Order order;
  private Double subtotal;
  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  @Min(value = 1, message = "Quantity must be at least 1")
  private Integer quantity;

  public OrderItem(Product product, Integer quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public double getSubtotal() {
    return product.getPrice() * quantity;
  }


}
