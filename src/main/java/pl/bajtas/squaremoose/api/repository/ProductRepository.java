package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import pl.bajtas.squaremoose.api.domain.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    /* Find by Product properties */
    // First method, by id is not needed
    Product findByName(String name);
    List<Product> findByNameContainsIgnoreCase(String name);
    List<Product> findByDescriptionContainsIgnoreCase(String description);
    // Find by Product.Price
    List<Product> findByPriceBetween(float price1, float price2);
    List<Product> findByPriceGreaterThan(float price1);
    List<Product> findByPriceLessThan(float price2);

    /* Find by Category properties */
    List<Product> findByCategoryIsNotNull();
    List<Product> findByCategoryIsNull();
    List<Product> findByCategory_NameContainsIgnoreCase(String name);
    List<Product> findByCategory_Id(int id);

    /* Find by Image properties */
    List<Product> findByImagesIsNotNull();
    List<Product> findByImagesIsNull();
    List<Product> findByImages_ImageSrcContainsIgnoreCase(String imageSrc);

    /* Category stats */
    long countByCategory_Id(int id);
    long countDistinctByCategory_Name(String name);

//    @Query("from Product a where a.category.id=:categoryid")
//    List<Product> findByCategoryId(@Param("categoryid") Integer categoryId);



}
