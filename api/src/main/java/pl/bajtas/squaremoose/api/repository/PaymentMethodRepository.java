package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.PaymentMethod;
import pl.bajtas.squaremoose.api.domain.Product;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Integer> {
    PaymentMethod findByName(String method);

    Page<PaymentMethod> findAll(Pageable pageable);
}
