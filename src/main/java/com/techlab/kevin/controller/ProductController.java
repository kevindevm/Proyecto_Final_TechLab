package com.techlab.kevin.controller;

import com.techlab.kevin.dto.BulkProductResponseDTO;
import com.techlab.kevin.dto.OrderApiResponseDTO;
import com.techlab.kevin.dto.ProductApiResponseDTO;
import com.techlab.kevin.entities.Product;
import com.techlab.kevin.services.ProductService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @Operation(summary = "Crear productos en masa")
  @ApiResponse(responseCode = "207", description = "Lista de productos creados")
  @PostMapping("/bulk")
  public ResponseEntity<BulkProductResponseDTO> bulkCreate(
      @Valid @RequestBody List<Product> productList) {
    BulkProductResponseDTO result = service.saveAll(productList);
    return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(result);
  }

  @Operation(summary = "Crear Producto")
  @ApiResponse(responseCode = "201", description = "Producto Creado")
  @PostMapping
  public ResponseEntity<ProductApiResponseDTO<Product>> create(@RequestBody Product product) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.addProduct(product));
  }

  @Operation(summary = "Actualizar Producto")
  @ApiResponse(responseCode = "200", description = "Producto Actualizado")
  @PatchMapping("/{id}")
  public ResponseEntity<ProductApiResponseDTO<Product>> update(@PathVariable Integer id,
      @Valid @RequestBody Product product) {
    return service.updateById(id, product);
  }

  @Operation(summary = "Obtener todos los productos")
  @ApiResponse(responseCode = "200", description = "lista de productos creados")
  @GetMapping
  public List<Product> getAll() {
    return service.productsList();
  }

  @Operation(summary = "Buscar por palabra o id")
  @ApiResponse(responseCode = "200", description = "lista de productos encontrados")
  @GetMapping("/search/{keyword}")
  public ResponseEntity<Object> search(@PathVariable String keyword) {
    try {
      int id = Integer.parseInt(keyword);
      ProductApiResponseDTO<Product> result = service.productSearchByID(id);
      return ResponseEntity.ok(result);
    } catch (NumberFormatException e) {
      ProductApiResponseDTO<Product> result = service.productSearchByKeyword(keyword);
      if (result.getFoundProducts() == null || result.getFoundProducts() == 0) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ProductApiResponseDTO<>("No products found for keyword: " + keyword));
      }
      return ResponseEntity.ok(result);
    }
  }

  @Operation(summary = "Remover Producto")
  @ApiResponse(responseCode = "200", description = "lista de productos creados")
  @DeleteMapping("delete/{id}")
  public ResponseEntity<ProductApiResponseDTO<Product>> delete(@PathVariable Integer id) {
    return service.deleteById(id);
  }
}
