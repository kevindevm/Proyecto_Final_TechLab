package com.techlab.kevin.services;

import com.techlab.kevin.dto.BulkDTO;
import com.techlab.kevin.dto.BulkProductResponseDTO;
import com.techlab.kevin.dto.ProductApiResponseDTO;
import com.techlab.kevin.entities.Product;
import com.techlab.kevin.exceptions.ProductNotFoundException;
import com.techlab.kevin.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }


  public BulkProductResponseDTO saveAll(List<Product> products) {
    List<BulkDTO> created = new ArrayList<>();
    List<BulkDTO> failed = new ArrayList<>();

    for (Product p : products) {
      try {
        ProductApiResponseDTO<Product> result = this.addProduct(p);
        created.add(new BulkDTO(result.getId(), p.getName(), null));
      } catch (Exception e) {
        failed.add(new BulkDTO(null, p.getName(), e.getMessage()));
      }
    }

    return new BulkProductResponseDTO(created, failed);
  }

  public ProductApiResponseDTO<Product> productSearchByKeyword(String keyword) {
    List<Product> found = productRepository.searchByName(keyword);
    int total = (int) productRepository.count();

    if (found.isEmpty()) {
      return new ProductApiResponseDTO<>("No products found for keyword: " + keyword);
    }

    return new ProductApiResponseDTO<>(keyword, found, total);
  }

  public List<Product> productsList() {
    return this.productRepository.findAll();
  }


  public ProductApiResponseDTO<Product> addProduct(Product p) {

    if (p.getStock() < 0) {
      throw new IllegalArgumentException("stock cannot be negative");
    }
    if (p.getName() == null || p.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("product name cannot be empty");
    }
    if (p.getPrice() <= 0) {
      throw new IllegalArgumentException("price must be greater than zero");
    }
    p.setName(p.returnCapitalized(p.getName()));
    if (p.getName().length() > 70) {
      p.setName(p.getName().substring(0, 70));
    }
    if (productRepository.existsByName(p.getName())) {
      throw new IllegalArgumentException("duplicated name");
    }
    Product s = this.productRepository.save(p);
    return new ProductApiResponseDTO<>(
        "Producto creado correctamente",
        s.getId()
    );
  }

  public ProductApiResponseDTO<Product> productSearchByID(Integer id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));

    return new ProductApiResponseDTO<>("Producto encontrado correctamente", product);
  }


  public ResponseEntity<ProductApiResponseDTO<Product>> updateById(Integer id, Product updateData) {
    return productRepository.findById(id).map(existing -> {

      if (updateData.getName() != null && !updateData.getName().trim().isEmpty()) {
        existing.setName(updateData.getName());
      }

      if (updateData.getPrice() != null && updateData.getPrice() > 0) {
        existing.setPrice(updateData.getPrice());
      }

      if (updateData.getStock() != null && updateData.getStock() >= 0) {
        existing.setStock(updateData.getStock());
      }

      Product updated = productRepository.save(existing);
      return ResponseEntity.ok(
          new ProductApiResponseDTO<>("Product updated successfully", updated));

    }).orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
  }


  public ResponseEntity<ProductApiResponseDTO<Product>> deleteById(Integer id) {
    if (!productExistByID(id)) {
      ProductApiResponseDTO<Product> response = new ProductApiResponseDTO<>(
          "Producto con ID " + id + " no encontrado"
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    productRepository.deleteById(id);

    ProductApiResponseDTO<Product> response = new ProductApiResponseDTO<>(
        "Producto eliminado correctamente"
    );
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }


  public boolean productExistByID(Integer id) {
    return productRepository.existsById(id);

  }


}
