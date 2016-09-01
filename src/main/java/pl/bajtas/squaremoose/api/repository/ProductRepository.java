package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;

import pl.bajtas.squaremoose.api.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
  default public void sayHi() {
    System.out.println("Hi");
  }
}
