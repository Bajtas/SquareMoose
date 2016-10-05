package pl.bajtas.squaremoose.api.repository;

import org.springframework.cglib.core.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
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

    /* Search by all properties */
    @Query("FROM Product p WHERE lower(p.name) LIKE %:name% AND p.description=:description AND p.price>=:price1 AND p.price<=:price2 AND p.category.name=:categoryName")
    Page<Product> searchByAll(String name, String description, double price1, double price2, String categoryMame, Pageable page);
    /* Search by name */
    Page<Product> findByNameContainsIgnoreCase(String name, Pageable page);
    Page<Product> findByNameAndDescriptionContainsIgnoreCase(String name, Pageable page);
    /* Search by description */
    Page<Product> findByDescriptionContainsIgnoreCase(String name, Pageable page);
    /* Search by price */
    Page<Product> findByPriceBetween(double price1, double price2, Pageable page);
    Page<Product> findByPriceGreaterThanEqual(double price1, Pageable page);
    Page<Product> findByPriceLessThanEqual(double price2, Pageable page);

//    @Query("from Product a where a.category.id=:categoryid")
//    List<Product> findByCategoryId(@Param("categoryid") Integer categoryId);
}
