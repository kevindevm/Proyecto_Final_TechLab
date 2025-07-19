package com.techlab.kevin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinalApplication {

  public static void main(String[] args) {
    SpringApplication.run(FinalApplication.class, args);
  }

//    @Bean
//    CommandLineRunner initDatabase(ProductRepository productRepository) {
//        return args -> {
//            productRepository.save(new Product("Mouse Logitech G203", 13500.0, 25));
//            productRepository.save(new Product("Teclado Redragon Kumara K552", 24000.0, 15));
//            productRepository.save(new Product("Monitor Samsung 24'' LED 75Hz", 95000.0, 10));
//            productRepository.save(new Product("Notebook Lenovo IdeaPad 15.6'' i5", 650000.0, 5));
//            productRepository.save(new Product("Gabinete Corsair 4000D", 110000.0, 8));
//            productRepository.save(new Product("Placa de Video NVIDIA RTX 4060", 410000.0, 4));
//            productRepository.save(new Product("Disco SSD Kingston NV2 1TB", 38000.0, 20));
//            productRepository.save(new Product("Memoria RAM Corsair Vengeance 16GB DDR4", 45000.0, 12));
//            productRepository.save(new Product("Fuente EVGA 600W 80+ Bronze", 62000.0, 9));
//            productRepository.save(new Product("Auriculares HyperX Cloud Stinger", 30000.0, 18));
//            productRepository.save(new Product("Procesador AMD Ryzen 5 5600X", 120000.0, 6));
//            productRepository.save(new Product("Motherboard MSI B550M PRO", 100000.0, 7));
//            productRepository.save(new Product("Webcam Logitech C920 HD", 42000.0, 10));
//            productRepository.save(new Product("Impresora Epson EcoTank L3250", 170000.0, 3));
//            productRepository.save(new Product("Silla Gamer Corsair T3 Rush", 220000.0, 2));
//        };
//    }
}
