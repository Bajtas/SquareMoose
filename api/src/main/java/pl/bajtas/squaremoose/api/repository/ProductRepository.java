package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import pl.bajtas.squaremoose.api.domain.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    /* Find by Product properties */
    // First method, by id is not needed
    Product findByName(String name);
    List<Product> findByNameContainsIgnoreCase(String name);
    List<Product> findByDescriptionContainsIgnoreCase(String description);
    // Find by Product.Price
    List<Product> findByPriceBetween(double price1, double price2);
    List<Product> findByPriceGreaterThanEqual(double price1);
    List<Product> findByPriceLessThanEqual(double price2);

    /* Find by Category properties */
    Iterable<Product> findByCategoryIsNotNull();
    Iterable<Product> findByCategoryIsNull();
    List<Product> findByCategory_NameContainsIgnoreCase(String name);
    List<Product> findByCategory_Id(int id);

    /* Find by Image properties */
    List<Product> findByImagesIsNotNull();
    List<Product> findByImagesIsNull();
    List<Product> findByImages_ImageSrcContainsIgnoreCase(String imageSrc);

    /* Find by Order items properties */
    List<Product> findByOrderItems_Order_Id(int id);

    /* Category stats */
    long countByCategory_Id(int id);
    long countDistinctByCategory_Name(String name);
    long countDistinctByCategoryIsNotNull();
    long countDistinctByCategoryIsNull();

    Page<Product> findAll(Pageable pageable);

//    @Query("from Product a where a.category.id=:categoryid")
//    List<Product> findByCategoryId(@Param("categoryid") Integer categoryId);
}
