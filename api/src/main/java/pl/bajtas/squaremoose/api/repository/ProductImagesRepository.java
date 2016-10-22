package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.ProductImage;

import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */
public interface ProductImagesRepository extends CrudRepository<ProductImage, Integer> {
    ProductImage findByImageSrcContainsIgnoreCase(String src);

    List<ProductImage> getByProducts_NameContainsIgnoreCase(String productName);

    List<ProductImage> getByProducts_Id(Integer productId);
    /* Find by Product properties */
    // First method, by id is not needed
}
