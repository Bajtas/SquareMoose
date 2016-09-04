package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.DeliveryAdress;
import pl.bajtas.squaremoose.api.domain.OrderItem;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface DeliveryAdressRepository  extends CrudRepository<DeliveryAdress, Integer> {
}
