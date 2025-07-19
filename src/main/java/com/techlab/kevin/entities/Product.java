package com.techlab.kevin.entities;

import com.techlab.kevin.exceptions.NoStockException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Product {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Integer id;

  @Min(0)
  private Integer stock;
  private Double price;
  private String name;


  public Product(String name, Double price, Integer stock) {
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  public void updateStock(int amount) throws NoStockException {
    if (this.stock + amount < 0) {
      throw new NoStockException("No hay suficiente stock para el producto con ID: " + this.id);
    }

    this.stock += amount;
  }


  public String returnCapitalized(String arg) {

    String resultado = arg.toLowerCase().trim();
    String[] partes = resultado.replaceAll(" +", " ").split(" ");
    StringBuilder NewCadena = new StringBuilder(resultado.length());

    for (String parteActual : partes) {
      NewCadena.append(parteActual.toUpperCase().charAt(0)).append(parteActual.substring(1))
          .append(" ");
    }
    return NewCadena.toString().trim();


  }


}
