package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.Order;

import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface OrderRepository extends CrudRepository<Order, Integer> {

    Page<Order> findAll(Pageable pageRequest);

    Order findByUser_Id(int id);
    Order findByUser_Login(String login);
    Order findByDeliveryAdress_Id(int id);
    Order findByDeliveryType_Id(int id);
    List<Order> findByPaymentMethod_Id(int id);

    Order findDistinctById(int id);
}
