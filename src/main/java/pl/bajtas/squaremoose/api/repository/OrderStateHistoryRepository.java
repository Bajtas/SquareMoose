package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.OrderStateHistory;
import pl.bajtas.squaremoose.api.domain.PaymentMethod;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface OrderStateHistoryRepository extends CrudRepository<OrderStateHistory, Integer> {
}
