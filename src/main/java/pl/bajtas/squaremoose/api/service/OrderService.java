package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.domain.OrderItem;
import pl.bajtas.squaremoose.api.repository.OrderItemRepository;
import pl.bajtas.squaremoose.api.repository.OrderRepository;

import javax.persistence.EntityManager;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class OrderService {
    private static final Logger LOG = Logger.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;
    //@Autowired  private ProductRepository productRepository;
    @Autowired  private EntityManager entityManager;


    public Iterable<Order> getAll() {
        return orderRepository.findAll();
    }
}
