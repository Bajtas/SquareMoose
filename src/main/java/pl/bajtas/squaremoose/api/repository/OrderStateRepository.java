package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.OrderState;
import pl.bajtas.squaremoose.api.domain.PaymentMethod;

import java.util.List;

/**
 * Created by Bajtas on 18.09.2016.
 */
public interface OrderStateRepository extends CrudRepository<OrderState, Integer> {
    /* Find by Product properties */
    // First method, by id is not needed
    //ActualOrderState findByName(String name);
    List<OrderState> findByNameContainsIgnoreCase(String name);
    List<OrderState> findByDescriptionContainsIgnoreCase(String description);
    Page<OrderState> findAll(Pageable pageable);

    /* Find by Order properties */
    //ActualOrderState findByOrder_Id(int id);
}
