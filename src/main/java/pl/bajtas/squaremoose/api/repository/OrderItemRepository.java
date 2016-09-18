package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.OrderItem;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface OrderItemRepository extends CrudRepository<OrderItem, Integer> {

    Page<OrderItem> findAll(Pageable pageRequest);
}
