package com.techlab.kevin.entities;

import com.techlab.kevin.exceptions.NoStockException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String status;

  private Double totalAmount;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  public Order(Product product, Integer quantity) {
    this.addItem(product, quantity);
  }

  public void addItem(Product product, Integer quantity) {
    for (OrderItem item : this.items) {
      if (item.getProduct().getId().equals(product.getId())) {
        item.setQuantity(item.getQuantity() + quantity);
        updateTotal();
        return;
      }
    }

    try {
      product.updateStock(-quantity);
    } catch (NoStockException e) {
      throw new NoStockException("Not enough stock for product ID: " + product.getId());
    }

    OrderItem newItem = new OrderItem(product, quantity);
    newItem.setOrder(this);
    this.items.add(newItem);
    updateTotal();
  }

  public void removeItemByProductId(Integer productId) {
    boolean removed = this.items.removeIf(item -> item.getProduct().getId().equals(productId));
      if (removed) {
          updateTotal();
      }
  }

  public void removeItemByProductName(String name) {
    boolean removed = this.items.removeIf(
        item -> item.getProduct().getName().equalsIgnoreCase(name));
      if (removed) {
          updateTotal();
      }
  }

  public void updateTotal() {
    this.totalAmount = this.items.stream()
        .mapToDouble(OrderItem::getSubtotal)
        .sum();
  }
}
