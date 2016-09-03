package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.domain.ProductImage;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface OrderRepository extends CrudRepository<Order, Integer> {
}
