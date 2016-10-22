package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.DeliveryType;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.repository.DeliveryTypeRepository;
import pl.bajtas.squaremoose.api.repository.OrderRepository;
import pl.bajtas.squaremoose.api.service.generic.GenericService;
import pl.bajtas.squaremoose.api.util.search.PageUtil;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Bajtas on 21.09.2016.
 */

@Service
public class DeliveryTypeService implements GenericService<DeliveryType, DeliveryTypeRepository>, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(DeliveryTypeService.class);

    @Autowired private DeliveryTypeRepository deliveryTypeRepository;
    @Autowired private OrderRepository orderRepository;

    @Override
    public DeliveryTypeRepository getRepository() {
        return deliveryTypeRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    @Override
    public Iterable<DeliveryType> getAll() {
        return deliveryTypeRepository.findAll();
    }

    @Override
    public Page<DeliveryType> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        PageUtil<DeliveryType> util = new PageUtil<>();
        return util.getPage(page, size, sortBy, sortDirection, getRepository());
    }

    @Override
    public DeliveryType getById(int id) {
        return deliveryTypeRepository.findOne(id);
    }

    @Override
    public Response add(DeliveryType deliveryType) {
        LOG.info("Trying to save DeliveryType: " + deliveryType.getId());

        if (isDeliveryTypeExists(deliveryType.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: DeliveryType with id: " + deliveryType.getId() + " already exist.").build();
        }

        try {
            getRepository().save(deliveryType);
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occured when saved: " + e.toString()).build();
        }

        LOG.info("Object added successfully!");
        return Response.status(Response.Status.OK).entity("DeliveryType added successfully!").build();
    }

    @Override
    public Response update(int id, DeliveryType deliveryType) {
        LOG.info("Trying to save delivery type with id: " + id);

        try {
            DeliveryType old = getRepository().findOne(id);
            if (old != null) {
                deliveryType.setId(id);

                getRepository().save(deliveryType);
                return Response.status(Response.Status.OK).entity("DeliveryType with id: " + id + " updated successfully!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("DeliveryType with given id: " + id + " not found!").build();
            }
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }
    }

    @Override
    public Response delete(int id) {
        LOG.info("Trying to delete DeliveryType.");
        String info = "Deleted successfully!";

        LOG.info("DeliveryType with id: " + id + " will be deleted.");

        DeliveryType deliveryType = getRepository().findOne(id);
        if (deliveryType != null) {
            try {
                List<Order> orders = deliveryType.getOrders();
                if (orders.size() != 0) {
                    for (Order order : orders) {
                        order.setDeliveryType(null);
                        orderRepository.save(order);
                    }

                    getRepository().save(deliveryType);
                }
                getRepository().delete(id);
                return Response.status(Response.Status.OK).entity("DeliveryType with id: " + id + " deleted successfully!").build();
            } catch (Exception e) {
                LOG.error("Category with id: " + id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("DeliveryType with given id: " + id + " not found!").build();
        }
    }

    private boolean isDeliveryTypeExists(Integer id) {
        DeliveryType deliveryType = deliveryTypeRepository.findOne(id);
        if (deliveryType != null)
            return true;
        return false;
    }
}
