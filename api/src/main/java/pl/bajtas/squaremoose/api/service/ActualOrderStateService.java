package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.domain.OrderStateHistory;
import pl.bajtas.squaremoose.api.repository.ActualOrderStateRepository;
import pl.bajtas.squaremoose.api.repository.OrderRepository;
import pl.bajtas.squaremoose.api.repository.OrderStateHistoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.service.generic.GenericService;
import pl.bajtas.squaremoose.api.util.search.PageUtil;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

//import pl.bajtas.squaremoose.api.domain.OrderState;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class ActualOrderStateService implements GenericService<ActualOrderState, ActualOrderStateRepository>, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(ActualOrderStateService.class);

    @Autowired
    private ActualOrderStateRepository actualOrderStateRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStateHistoryRepository orderStateHistoryRepository;

    // Events
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    @Override
    public ActualOrderStateRepository getRepository() {
        return actualOrderStateRepository;
    }

    @Override
    public Iterable<ActualOrderState> getAll() {
        LOG.info("Getting all ActualOrderStates.");
        return getRepository().findAll();
    }

    @Override
    public Page<ActualOrderState> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        PageUtil<ActualOrderState> util = new PageUtil<>();
        return util.getPage(page, size, sortBy, sortDirection, getRepository());
    }

    @Override
    public ActualOrderState getById(int id) {
        return getRepository().findOne(id);
    }

    public List<ActualOrderState> getByNameContainsIgnoreCase(String name) {
        return getRepository().findByNameContainsIgnoreCase(name);
    }

    public List<ActualOrderState> getByDescriptionContainsIgnoreCase(String description) {
        return getRepository().findByDescriptionContainsIgnoreCase(description);
    }

    /* --------------------------------------------------------------------------------------------- */

    // Search by Order properties
    public ActualOrderState getByOrderId(int id) {
        return getRepository().findByOrder_Id(id);
    }

    // Search by User properties /* TO DO */
    public List<ActualOrderState> getByUserId(int id) {
        //orderRepository.
        return null;
    }

    /* --------------------------------------------------------------------------------------------- */

    // Add new ActualOrderState to DB
    @Override
    public Response add(ActualOrderState actualOrderState) {
        try {
            Order order = orderRepository.findOne(actualOrderState.getOrder().getId());
            if (order.getActualOrderState() == null) {
                actualOrderState.setOrder(order);
                actualOrderState.setLmod(new Date());
                actualOrderState.setDescription("");
            } else {
                OrderStateHistory oldState = new OrderStateHistory();
                oldState.setActualOrderState(order.getActualOrderState());
                oldState.setDescription("");
                oldState.setLmod(order.getActualOrderState().getLmod());
                oldState.setName(order.getActualOrderState().getName());
                orderStateHistoryRepository.save(oldState);

                order.getActualOrderState().setName(actualOrderState.getName());
                order.getActualOrderState().setLmod(new Date());
                order.getActualOrderState().getOrderStateHistories().add(oldState);
                getRepository().save(order.getActualOrderState());

                actualOrderState = order.getActualOrderState();
            }

            if (order.getActualOrderState() == null)
                getRepository().save(actualOrderState);

            order.setActualOrderState(actualOrderState);
            order.setLmod(new Date());
            orderRepository.save(order);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("ActualOrderState added successfully!").build();
    }

    // Update existing
    @Override
    public Response update(int id, ActualOrderState actualOrderState) {
        actualOrderState.setId(id);

        try {
            if (actualOrderState.getOrder() != null) {
                orderRepository.save(actualOrderState.getOrder());
            }
//            if (actualOrderState.getOrderState() != null) {
//                orderStateRepository.save(actualOrderState.getOrderState());
//            }
            if (actualOrderState.getOrderStateHistories() != null) {
                List<OrderStateHistory> histories = actualOrderState.getOrderStateHistories();
                for (OrderStateHistory history : histories)
                    orderStateHistoryRepository.save(history);
            }

            getRepository().save(actualOrderState);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("ActualOrderState with id: " + id + " updated successfully!").build();
    }

    // Delete
    @Override
    public Response delete(int id) {
        LOG.info("Trying to delete ActualOrderState.");

        LOG.info("ActualOrderState with id: " + id + " will be deleted.");

        ActualOrderState actualOrderState = getRepository().findOne(id);
        //OrderState state = actualOrderState.getOrderState();
        List<OrderStateHistory> histories = actualOrderState.getOrderStateHistories();
        if (actualOrderState != null) {
            try {
                Order order = actualOrderState.getOrder();
                if (order != null) {
                    order.setActualOrderState(null);
                    orderRepository.save(order);
                }
//                if (state != null) {
//                    List<ActualOrderState> actualOrderStates = new ArrayList<>();
//                    actualOrderStates.remove(actualOrderStates);
//                    state.setActualOrderStates(actualOrderStates);
//                    orderStateRepository.save(state);
//                }
                getRepository().delete(id);
                if (histories.size() != 0) {
                    for (OrderStateHistory history : histories)
                        orderStateHistoryRepository.delete(history.getId());
                }
                return Response.status(Response.Status.OK).entity("ActualOrderState with id: " + id + " deleted successfully!").build();
            } catch (Exception e) {
                LOG.error("ActualOrderState with id: " + id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("ActualOrderState with given id: " + id + " not found!").build();
        }
    }

    /* --------------------------------------------------------------------------------------------- */
}
