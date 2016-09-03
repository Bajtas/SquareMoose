package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.OrderItem;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.OrderItemRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;

import javax.persistence.EntityManager;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class OrderItemService {
    private static final Logger LOG = Logger.getLogger(OrderItemService.class);

    @Autowired
    private OrderItemRepository orderItemRepository;
    //@Autowired  private ProductRepository productRepository;
    @Autowired  private EntityManager entityManager;


    public Iterable<OrderItem> getAll() {
        return orderItemRepository.findAll();
    }
}
