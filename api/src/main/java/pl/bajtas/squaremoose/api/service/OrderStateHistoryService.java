package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.OrderStateHistory;
import pl.bajtas.squaremoose.api.repository.OrderStateHistoryRepository;

import javax.persistence.EntityManager;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class OrderStateHistoryService {
    private static final Logger LOG = Logger.getLogger(OrderStateHistoryService.class);

    @Autowired
    private OrderStateHistoryRepository orderStateHistoryRepository;
    //@Autowired  private ProductRepository productRepository;
    @Autowired  private EntityManager entityManager;

    public Iterable<OrderStateHistory> getAll() {
        return orderStateHistoryRepository.findAll();
    }
}
