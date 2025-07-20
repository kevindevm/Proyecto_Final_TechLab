package com.techlab.kevin.controller;

import com.techlab.kevin.dto.BulkProductResponseDTO;
import com.techlab.kevin.dto.ProductApiResponseDTO;
import com.techlab.kevin.entities.Product;
import com.techlab.kevin.services.ProductService;
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

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }


  @PostMapping("/bulk")
  public ResponseEntity<BulkProductResponseDTO> bulkCreate(
      @Valid @RequestBody List<Product> productList) {
    BulkProductResponseDTO result = service.saveAll(productList);
    return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(result);
  }


  @PostMapping
  public ResponseEntity<ProductApiResponseDTO<Product>> create(@RequestBody Product product) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.addProduct(product));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ProductApiResponseDTO<Product>> update(@PathVariable Integer id,
      @Valid @RequestBody Product product) {
    return service.updateById(id, product);
  }

  @GetMapping
  public List<Product> getAll() {
    return service.productsList();
  }

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

  @DeleteMapping("delete/{id}")
  public ResponseEntity<ProductApiResponseDTO<Product>> delete(@PathVariable Integer id) {
    return service.deleteById(id);
  }
}
