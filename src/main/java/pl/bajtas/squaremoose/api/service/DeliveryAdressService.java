package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.DeliveryAdress;
import pl.bajtas.squaremoose.api.domain.OrderItem;
import pl.bajtas.squaremoose.api.repository.DeliveryAdressRepository;
import pl.bajtas.squaremoose.api.repository.OrderItemRepository;

import javax.persistence.EntityManager;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class DeliveryAdressService {

    private static final Logger LOG = Logger.getLogger(DeliveryAdressService.class);

    @Autowired
    private DeliveryAdressRepository deliveryAdressRepository;
    //@Autowired  private ProductRepository productRepository;
    @Autowired  private EntityManager entityManager;


    public Iterable<DeliveryAdress> getAll() {
        return deliveryAdressRepository.findAll();
    }

}
