package com.techlab.kevin.controller;

import com.techlab.kevin.dto.OrderApiResponseDTO;
import com.techlab.kevin.entities.Order;
import com.techlab.kevin.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Órdenes", description = "Operaciones relacionadas con órdenes")
@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService service;

  public OrderController(OrderService service) {
    this.service = service;
  }

  @Operation(summary = "Crear una nueva orden")
  @ApiResponse(responseCode = "201", description = "órden creada ")
  @PostMapping
  public ResponseEntity<OrderApiResponseDTO> create(@Valid @RequestBody Order order) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.addOrder(order));
  }

  @Operation(summary = "Obtener todas las órdenes")
  @ApiResponse(responseCode = "200", description = "lista de todas las órdenes",
             content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
  @GetMapping
  public List<Order> getAll() {
    return service.getAllOrders();
  }

  @Operation(summary = "Obtener una orden por ID")
  @ApiResponse(responseCode = "200", description = "Orden encontrada",
             content = @Content(mediaType = "application/json"))
  @GetMapping("/{id}")
  public ResponseEntity<OrderApiResponseDTO> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @Operation(summary = "Actualizar una orden existente")
  @ApiResponse(responseCode = "200", description = "Orden actualizada",
             content = @Content(mediaType = "application/json"))
  @PutMapping("/{id}")
  public ResponseEntity<OrderApiResponseDTO> update(@PathVariable Integer id,
      @Valid @RequestBody Order order) {
    return ResponseEntity.ok(service.updateOrder(id, order));
  }

  @Operation(summary = "Actualizar cantidad de un item en la orden")
  @ApiResponse(responseCode = "200", description = "Cantidad actualizada",
             content = @Content(mediaType = "application/json"))
  @PatchMapping("/{orderId}/items/{productId}")
  public ResponseEntity<?> updateItemQuantity(
      @PathVariable Integer orderId,
      @PathVariable Integer productId,
      @RequestParam Integer quantity) {
    return ResponseEntity.ok(service.updateItemQuantity(orderId, productId, quantity));
  }

  @Operation(summary = "Eliminar un producto de la orden")
  @ApiResponse(responseCode = "200", description = "Producto eliminado de la orden",
             content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
  @DeleteMapping("/{orderId}/product/{productId}")
  public ResponseEntity<?> removeProductFromOrder(@PathVariable Integer orderId,
      @PathVariable Integer productId) {
    Order updated = service.removeItemByProduct(orderId, productId);
    return ResponseEntity.ok(updated);
  }

  @Operation(summary = "Eliminar una orden")
  @ApiResponse(responseCode = "200", description = "Orden eliminada",
             content = @Content(mediaType = "application/json"))
  @DeleteMapping("/{id}")
  public ResponseEntity<OrderApiResponseDTO> delete(@PathVariable Integer id) {
    return ResponseEntity.ok(service.deleteOrder(id));
  }
}