package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer>{
    /* Find by Product properties */
    // First method, by id is not needed
    Iterable<Category> findByNameContainsIgnoreCase(String name);

    Long deleteByName(String name);
}
