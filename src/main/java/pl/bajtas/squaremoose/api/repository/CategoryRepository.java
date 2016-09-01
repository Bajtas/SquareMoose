package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;

import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Product;

public interface CategoryRepository extends CrudRepository<Category, Integer>{

}
