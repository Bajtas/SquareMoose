package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.OrderItem;

import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface OrderItemRepository extends CrudRepository<OrderItem, Integer> {

    Page<OrderItem> findAll(Pageable pageRequest);
    List<OrderItem> findByOrder_Id(int id);
    List<OrderItem> findByOrder_User_Id(int id);
}
