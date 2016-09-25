package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.domain.OrderState;
import pl.bajtas.squaremoose.api.repository.ActualOrderStateRepository;
import pl.bajtas.squaremoose.api.repository.OrderRepository;
import pl.bajtas.squaremoose.api.repository.OrderStateRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.util.search.PageUtil;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import java.util.List;

/**
 * Created by Bajtas on 18.09.2016.
 */
@Service
public class OrderStateService implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(OrderStateService.class);

    @Autowired private ActualOrderStateRepository actualOrderStateRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderStateRepository orderStateRepository;

    public OrderStateRepository getRepository() {
        return orderStateRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    // Search by ActualOrderState properties
    public Iterable<OrderState> getAll() {
        LOG.info("Getting all OrderStates.");
        return getRepository().findAll();
    }

    public Page<OrderState> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        PageUtil<OrderState> util = new PageUtil<>();
        return util.getPage(page, size, sortBy, sortDirection, getRepository());
    }

    public OrderState getById(int id) {
        return getRepository().findOne(id);
    }

    public List<OrderState> getByNameContainsIgnoreCase(String name) {
        return getRepository().findByNameContainsIgnoreCase(name);
    }

    public List<OrderState> getByDescriptionContainsIgnoreCase(String description) {
        return getRepository().findByDescriptionContainsIgnoreCase(description);
    }

    /* --------------------------------------------------------------------------------------------- */

//    // Search by Order properties
//    public ActualOrderState getByOrderId(int id) {
//        return getRepository().findByOrder_Id(id);
//    }
//
//    // Search by User properties /* TO DO */
//    public List<ActualOrderState> getByUserId(int id) {
//        //orderRepository.
//        return null;
//    }

    /* --------------------------------------------------------------------------------------------- */

    // Add new ActualOrderState to DB
    public String add(OrderState orderState) {
        try {
            getRepository().save(orderState);
        } catch (Exception e) {
            return "Error: " + e;
        }

        return "ActualOrderState added successfully! Id: " + orderState.getId();
    }

    // Update existing
    public String update(int id, OrderState orderState) {
        orderState.setId(id);

        try {
            getRepository().save(orderState);
        } catch (Exception e) {
            return "Error: " + e;
        }

        return "ActualOrderState updated!";
    }

    // Delete
    public String delete(int id) {
        LOG.info("Trying to delete ActualOrderState.");
        String info = "Deleted successfully!";

        LOG.info("ActualOrderState with id: " + id + " will be deleted.");

        OrderState orderState = getRepository().findOne(id);
        if (orderState != null) {
            try {
                List<ActualOrderState> actualOrderStates = orderState.getActualOrderStates();
                if (actualOrderStates != null)
                {
                    for (ActualOrderState actualOrderState : actualOrderStates) {
                        actualOrderState.setOrderState(null);
                        actualOrderStateRepository.save(actualOrderState);
                    }
                }
                getRepository().delete(id);
            } catch (Exception e) {
                LOG.error("ActualOrderState with id: " + id, e);
                info = "Error occured!";
            }
        } else {
            info = "ActualOrderState with given id: " + id + " not found!";
        }
        return info;
    }
}
