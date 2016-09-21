package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.DeliveryAdress;
import pl.bajtas.squaremoose.api.domain.DeliveryType;
import pl.bajtas.squaremoose.api.domain.OrderItem;

/**
 * Created by Bajtas on 18.09.2016.
 */
public interface DeliveryTypeRepository extends CrudRepository<DeliveryType, Integer> {
    Page<DeliveryType> findAll(Pageable pageRequest);
}
