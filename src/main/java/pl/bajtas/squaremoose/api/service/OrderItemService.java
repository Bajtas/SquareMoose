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
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.OrderItemRepository;
import pl.bajtas.squaremoose.api.repository.OrderRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.service.generic.GenericService;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class OrderItemService implements GenericService<OrderItem, OrderItemRepository>, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(OrderItemService.class);

    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    @Override
    public OrderItemRepository getRepository() {
        return orderItemRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    @Override
    public Iterable<OrderItem> getAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public Page<OrderItem> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        boolean unsorted = false;
        Sort.Direction direction;

        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (StringUtils.isEmpty(sortBy))
            unsorted = true;

        Page<OrderItem> result;
        if (!unsorted) {
            direction = SearchUtil.determineSortDirection(sortDirection);

            result = getRepository().findAll(new PageRequest(page, size, direction, sortBy));
        }
        else
            result = getRepository().findAll(new PageRequest(page, size));

        return result;
    }

    @Override
    public OrderItem getById(int id) {
        LOG.info("Returns OrderItem related to id: " + id);

        return getRepository().findOne(id);
    }

    @Override
    public Response add(OrderItem orderItem) {
        LOG.info("Trying to save OrderItem: " + orderItem.getId());

        if (isOrderItemExists(orderItem.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: OrderItem with id: " + orderItem.getId() + " already exist.").build();
        }

        try {
            getRepository().save(orderItem);
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occured when saved: " + e.toString()).build();
        }

        LOG.info("Object added successfully!");
        return Response.status(Response.Status.OK).entity("OrderItem added successfully!").build();
    }

    @Override
    public Response update(int id, OrderItem orderItem) {
        LOG.info("Trying to save order item with id: " + id);

        try {
            OrderItem old = getRepository().findOne(id);
            if (old != null) {
                orderItem.setId(id);

                getRepository().save(orderItem);
                return Response.status(Response.Status.OK).entity("OrderItem with id: " + id + " updated successfully!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("OrderItem with given id: " + id + " not found!").build();
            }
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }
    }

    @Override
    public Response delete(int id) {
        LOG.info("Trying to delete OrderItem.");
        String info = "Deleted successfully!";

        LOG.info("OrderItem with id: " + id + " will be deleted.");

        OrderItem orderItem = getRepository().findOne(id);
        if (orderItem != null) {
            try {
                Order order = orderItem.getOrder();
                if (order != null) {
                    List<OrderItem> items = order.getOrderItems();
                    items.remove(orderItem);

                    orderRepository.save(order);
                }
                getRepository().delete(id);
                return Response.status(Response.Status.OK).entity("OrderItem with id: " + id + " deleted successfully!").build();
            } catch (Exception e) {
                LOG.error("Category with id: " + id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("OrderItem with given id: " + id + " not found!").build();
        }
    }

    private boolean isOrderItemExists(Integer id) {
        OrderItem orderItem = orderItemRepository.findOne(id);
        if (orderItem != null)
            return true;
        return false;
    }
}
