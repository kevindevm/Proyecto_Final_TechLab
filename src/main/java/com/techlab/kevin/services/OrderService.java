package com.techlab.kevin.services;

import com.techlab.kevin.dto.OrderApiResponseDTO;
import com.techlab.kevin.entities.Order;
import com.techlab.kevin.entities.OrderItem;
import com.techlab.kevin.entities.Product;
import com.techlab.kevin.exceptions.NoStockException;
import com.techlab.kevin.exceptions.OrderNotFoundException;
import com.techlab.kevin.exceptions.ProductNotFoundException;
import com.techlab.kevin.repository.OrderRepository;
import com.techlab.kevin.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
  }

  public OrderApiResponseDTO addOrder(Order order) {
    if (order.getItems() == null || order.getItems().isEmpty()) {
      throw new IllegalArgumentException("Order must contain at least one item.");
    }

    double total = 0.0;

    for (OrderItem item : order.getItems()) {
      Product product = productRepository.findById(item.getProduct().getId())
          .orElseThrow(() -> new ProductNotFoundException(
              "Product with ID " + item.getProduct().getId() + " not found."));

      if (product.getStock() < item.getQuantity()) {
        throw new NoStockException("Insufficient stock for product: " + product.getName());
      }

      product.updateStock(-item.getQuantity());
      item.setProduct(product);
      item.setOrder(order);

      total += product.getPrice() * item.getQuantity();
    }

    order.setTotalAmount(total);
    Order saved = orderRepository.save(order);

    return OrderApiResponseDTO.MsgAndOrder("Order created successfully", saved);
  }

  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public OrderApiResponseDTO findById(Integer id) {
    Order found = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));
    return OrderApiResponseDTO.MsgAndOrder("Order found", found);
  }

  public OrderApiResponseDTO updateOrder(Integer id, Order updatedData) {
    Order existing = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));

    if (updatedData.getStatus() != null) {
      existing.setStatus(updatedData.getStatus());
    }

    Order saved = orderRepository.save(existing);
    return OrderApiResponseDTO.MsgAndOrder("Order updated successfully", saved);
  }


  public OrderApiResponseDTO deleteOrder(Integer id) {
    if (!orderRepository.existsById(id)) {
      throw new OrderNotFoundException("Order with ID " + id + " not found");
    }

    orderRepository.deleteById(id);
    return OrderApiResponseDTO.MsgAndID("Order deleted successfully", id);
  }



  public Order removeItemByProduct(Integer orderId, Integer productId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException("Order not found"));

    boolean removed = order.getItems().removeIf(
        item -> item.getProduct().getId().equals(productId)
    );

    if (!removed) {
      throw new ProductNotFoundException("Product with ID " + productId + " not found in order.");
    }

    double total = order.getItems().stream()
        .mapToDouble(OrderItem::getSubtotal)
        .sum();
    order.setTotalAmount(total);

    return orderRepository.save(order);
  }

  public OrderItem updateItemQuantity(Integer orderId, Integer productId, Integer quantity) {

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException("Order not found"));

    OrderItem item = order.getItems().stream()
        .filter(i -> i.getProduct().getId().equals(productId))
        .findFirst()
        .orElseThrow(() -> new ProductNotFoundException("Product not found in order"));

    int prevQuantity = item.getQuantity();
    int diff = quantity - prevQuantity;

    Product product = item.getProduct();

    if (diff > 0 && product.getStock() < diff) {
      throw new RuntimeException("Not enough stock available");
    }

    product.setStock(product.getStock() - diff);

    item.setQuantity(quantity);
    item.setSubtotal(quantity * product.getPrice());

    double total = order.getItems().stream()
        .mapToDouble(OrderItem::getSubtotal)
        .sum();
    order.setTotalAmount(total);

    orderRepository.save(order);
    return item;
  }
}
