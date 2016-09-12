package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import pl.bajtas.squaremoose.api.domain.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.PaymentMethod;
import pl.bajtas.squaremoose.api.repository.OrderItemRepository;
import pl.bajtas.squaremoose.api.repository.PaymentMethodRepository;

import javax.persistence.EntityManager;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class PaymentMethodService {

    private static final Logger LOG = Logger.getLogger(PaymentMethodService.class);

    @Autowired  private PaymentMethodRepository paymentMethodRepository;

    public Iterable<PaymentMethod> getAll() {
        return paymentMethodRepository.findAll();
    }
}
