package com.techlab.kevin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.techlab.kevin.entities.Order;
import com.techlab.kevin.entities.OrderItem;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderApiResponseDTO {

  private String message;
  private Integer id;
  private Integer productId;
  private Integer quantity;
  private Double subtotal;
  private Double newSubtotal;
  private Order order;
  private String timestamp;
  private String status;
  private List<OrderItem> items;
  private Double totalAmount;

  public OrderApiResponseDTO() {
    this.timestamp = LocalDateTime.now().toString();
  }

  public static OrderApiResponseDTO OnlyMsg(String msg) {
    OrderApiResponseDTO dto = new OrderApiResponseDTO();
    dto.setMessage(msg);
    return dto;
  }

  public static OrderApiResponseDTO MsgAndID(String msg, Integer id) {
    OrderApiResponseDTO dto = new OrderApiResponseDTO();
    dto.setMessage(msg);
    dto.setId(id);
    return dto;
  }

  public static OrderApiResponseDTO MsgAndOrder(String msg, Order order) {
    OrderApiResponseDTO dto = new OrderApiResponseDTO();
    dto.setMessage(msg);
    dto.setOrder(order);
    return dto;
  }

  public static OrderApiResponseDTO MsgCreateOrUpdate(String msg, Integer id, String status,
      List<OrderItem> items,
      Double totalAmount) {
    OrderApiResponseDTO dto = new OrderApiResponseDTO();
    dto.setMessage(msg);
    dto.setId(id);
    dto.setStatus(status);
    dto.setItems(items);
    dto.setTotalAmount(totalAmount);
    return dto;
  }

  public static OrderApiResponseDTO updateItemQuantity(String msg, Integer orderId
      , Integer productId,
      @Min(value = 1, message = "Quantity must be at least 1") Integer quantity, double subtotal) {
    OrderApiResponseDTO dto = new OrderApiResponseDTO();
    dto.setMessage(msg);
    dto.setProductId(productId);
    dto.setNewSubtotal(subtotal);
    dto.setId(orderId);
    dto.setQuantity(quantity);
    dto.setSubtotal(subtotal);
    return dto;

  }


}
