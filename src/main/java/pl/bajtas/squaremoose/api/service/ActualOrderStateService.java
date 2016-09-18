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
import pl.bajtas.squaremoose.api.domain.*;
import pl.bajtas.squaremoose.api.repository.ActualOrderStateRepository;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.OrderRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class ActualOrderStateService implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(ActualOrderStateService.class);

    @Autowired private ActualOrderStateRepository actualOrderStateRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    public ActualOrderStateRepository getRepository() {
        return actualOrderStateRepository;
    }

    // Events
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    // Search by ActualOrderState properties
    public Iterable<ActualOrderState> getAll() {
        LOG.info("Getting all ActualOrderStates.");
        return getRepository().findAll();
    }

    public Page<ActualOrderState> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        boolean unsorted = false;
        Sort.Direction direction;

        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (StringUtils.isEmpty(sortBy))
            unsorted = true;

        Page<ActualOrderState> result;
        if (!unsorted) {
            direction = SearchUtil.determineSortDirection(sortDirection);

            result = getRepository().findAll(new PageRequest(page, size, direction, sortBy));
        }
        else
            result = getRepository().findAll(new PageRequest(page, size));

        return result;
    }

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
    public Response add(ActualOrderState actualOrderState) {
        try {
            getRepository().save(actualOrderState);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("ActualOrderState added successfully!").build();
    }

    // Update existing
    public String update(int id, ActualOrderState actualOrderState) {
        actualOrderState.setId(id);

        try {
            if (actualOrderState.getOrder() != null) {
                orderRepository.save(actualOrderState.getOrder());
            }

            getRepository().save(actualOrderState);
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

        ActualOrderState actualOrderState = getRepository().findOne(id);
        if (actualOrderState != null) {
            try {
                Order order = actualOrderState.getOrder();
                if (order != null)
                {
                    order.setActualOrderState(null);
                    orderRepository.save(order);
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

    /* --------------------------------------------------------------------------------------------- */
}
