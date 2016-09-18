package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.DeliveryAdress;
import pl.bajtas.squaremoose.api.domain.OrderItem;
import pl.bajtas.squaremoose.api.domain.Product;

import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface DeliveryAdressRepository  extends CrudRepository<DeliveryAdress, Integer> {
    /* Find by Product properties */
    // First method, by id is not needed
//    Product findByName(String name);
//    List<Product> findByNameContainsIgnoreCase(String name);
//    List<Product> findByDescriptionContainsIgnoreCase(String description);
//    // Find by Product.Price
//    List<Product> findByPriceBetween(double price1, double price2);
//    List<Product> findByPriceGreaterThanEqual(double price1);
//    List<Product> findByPriceLessThanEqual(double price2);
//
//    /* Find by Category properties */
//    Iterable<Product> findByCategoryIsNotNull();
//    Iterable<Product> findByCategoryIsNull();
//    List<Product> findByCategory_NameContainsIgnoreCase(String name);
//    List<Product> findByCategory_Id(int id);
//
//    /* Find by Image properties */
//    List<Product> findByImagesIsNotNull();
//    List<Product> findByImagesIsNull();
//    List<Product> findByImages_ImageSrcContainsIgnoreCase(String imageSrc);
//
//    /* Find by Order items properties */
//    List<Product> findByOrderItems_Order_Id(int id);
//
//    /* Category stats */
//    long countByCategory_Id(int id);
//    long countDistinctByCategory_Name(String name);
//    long countDistinctByCategoryIsNotNull();
//    long countDistinctByCategoryIsNull();

    Page<DeliveryAdress> findAll(Pageable pageable);
    Page<DeliveryAdress> findByOrdersIsNotNull(Pageable pageable);
    Page<DeliveryAdress> findByOrdersIsNull(Pageable pageable);

    List<DeliveryAdress> findByAdressContainsIgnoreCase(String adress);
    List<DeliveryAdress> findByTownContainsIgnoreCase(String town);
    List<DeliveryAdress> findByZipCodeContainsIgnoreCase(String zipCode);
    List<DeliveryAdress> findByNameContainsIgnoreCase(String name);
    List<DeliveryAdress> findBySurnameContainsIgnoreCase(String surname);
    List<DeliveryAdress> findByContactPhoneContainsIgnoreCase(String phone);

    List<DeliveryAdress> findByOrders_Id(int id);
    List<DeliveryAdress> findByUsers_Id(int id);
    List<DeliveryAdress> findByUsers_Login(String login);
    List<DeliveryAdress> findByUsers_Email(String email);

}
