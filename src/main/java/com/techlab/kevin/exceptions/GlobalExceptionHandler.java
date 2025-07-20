package com.techlab.kevin.exceptions;

import com.techlab.kevin.dto.OrderApiResponseDTO;
import com.techlab.kevin.dto.ProductApiResponseDTO;
import com.techlab.kevin.entities.Product;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProductApiResponseDTO<Product>> handleIllegalArgument(
      IllegalArgumentException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ProductApiResponseDTO<>(ex.getMessage()));
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ProductApiResponseDTO<Product>> handleProductNotFound(
      ProductNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ProductApiResponseDTO<>(ex.getMessage()));
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<OrderApiResponseDTO> handleOrderNotFound(OrderNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(OrderApiResponseDTO.OnlyMsg(ex.getMessage()));
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<OrderApiResponseDTO> handleMissingParam(
      MethodArgumentNotValidException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
            OrderApiResponseDTO.OnlyMsg("Parámetro requerido faltante: " + ex.getMessage()));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<OrderApiResponseDTO> handleMissingParam(
      MissingServletRequestParameterException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
            OrderApiResponseDTO.OnlyMsg("Parámetro requerido faltante: " + ex.getParameterName()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneralException(Exception ex) {
    if (ex instanceof MissingServletRequestParameterException e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(
              OrderApiResponseDTO.OnlyMsg("Parámetro requerido faltante: " + e.getParameterName()));
    }
    if (ex instanceof ProductNotFoundException || ex instanceof IllegalArgumentException) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ProductApiResponseDTO<>("Error interno del servidor: " + ex.getMessage()));
    }
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(OrderApiResponseDTO.OnlyMsg("Error interno del servidor: " + ex.getMessage()));
  }
}
