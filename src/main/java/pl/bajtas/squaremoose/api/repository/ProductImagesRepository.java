package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.ProductImage;

/**
 * Created by Bajtas on 03.09.2016.
 */
public interface ProductImagesRepository extends CrudRepository<ProductImage, Integer> {
    /* Find by Product properties */
    // First method, by id is not needed
}
