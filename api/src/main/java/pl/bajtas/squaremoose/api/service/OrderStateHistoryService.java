package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.OrderStateHistory;
import pl.bajtas.squaremoose.api.repository.OrderStateHistoryRepository;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class OrderStateHistoryService {
    private static final Logger LOG = Logger.getLogger(OrderStateHistoryService.class);

    @Autowired
    private OrderStateHistoryRepository orderStateHistoryRepository;
    @Autowired
    private EntityManager entityManager;

    public Iterable<OrderStateHistory> getAll() {
        return orderStateHistoryRepository.findAll();
    }

    public Response delete(int id) {
        orderStateHistoryRepository.delete(id);
        return Response.status(Response.Status.OK).entity("Order state history has been deleted successfully!").build();
    }
}
