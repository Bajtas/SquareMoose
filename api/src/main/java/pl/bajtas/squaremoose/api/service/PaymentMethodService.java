package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.domain.PaymentMethod;
import pl.bajtas.squaremoose.api.repository.OrderRepository;
import pl.bajtas.squaremoose.api.repository.PaymentMethodRepository;
import pl.bajtas.squaremoose.api.service.generic.GenericService;
import pl.bajtas.squaremoose.api.util.search.PageUtil;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class PaymentMethodService implements GenericService<PaymentMethod, PaymentMethodRepository>, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = Logger.getLogger(PaymentMethodService.class);

    private static final String[] DEFAULT_PAYMENT_METHODS = {"Credit card", "Cash", "Cash transfer", "PayPal"};

    @Autowired private PaymentMethodRepository paymentMethodRepository;
    @Autowired private OrderRepository orderRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for(String method: DEFAULT_PAYMENT_METHODS) {
            PaymentMethod paymentMethod = getRepository().findByName(method);
            if (paymentMethod == null) {
                paymentMethod = new PaymentMethod();
                paymentMethod.setName(method);
                getRepository().save(paymentMethod);
            }
        }
    }

    @Override
    public PaymentMethodRepository getRepository() {
        return paymentMethodRepository;
    }

    public Iterable<PaymentMethod> getAll() {
        return getRepository().findAll();
    }

    @Override
    public Page<PaymentMethod> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        PageUtil<PaymentMethod> util = new PageUtil<>();
        return util.getPage(page, size, sortBy, sortDirection, getRepository());
    }

    @Override
    public PaymentMethod getById(int id) {
        return getRepository().findOne(id);
    }

    @Override
    public Response add(PaymentMethod paymentMethod) {
        LOG.info("Trying to save PaymentMethod: " + paymentMethod.getId());

        if (isPaymentMethodExists(paymentMethod.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: PaymentMethod with id: " + paymentMethod.getId() + " already exist.").build();
        }

        try {
            getRepository().save(paymentMethod);
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occured when saved: " + e.toString()).build();
        }

        LOG.info("Object added successfully!");
        return Response.status(Response.Status.OK).entity("PaymentMethod added successfully!").build();
    }

    @Override
    public Response update(int id, PaymentMethod paymentMethod) {
        LOG.info("Trying to save delivery type with id: " + id);

        try {
            PaymentMethod old = getRepository().findOne(id);
            if (old != null) {
                paymentMethod.setId(id);

                getRepository().save(paymentMethod);
                return Response.status(Response.Status.OK).entity("PaymentMethod with id: " + id + " updated successfully!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("PaymentMethod with given id: " + id + " not found!").build();
            }
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }
    }

    @Override
    public Response delete(int id) {
        LOG.info("Trying to delete PaymentMethod.");
        String info = "Deleted successfully!";

        LOG.info("PaymentMethod with id: " + id + " will be deleted.");

        PaymentMethod paymentMethod = getRepository().findOne(id);
        if (paymentMethod != null) {
            try {
                List<Order> orders = orderRepository.findByPaymentMethod_Id(id);
                if (orders.size() != 0) {
                    for (Order order : orders) {
                        order.setPaymentMethod(null);
                        orderRepository.save(order);
                    }
                }
                getRepository().delete(id);
                return Response.status(Response.Status.OK).entity("PaymentMethod with id: " + id + " deleted successfully!").build();
            } catch (Exception e) {
                LOG.error("PaymentMethod with id: " + id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("PaymentMethod with given id: " + id + " not found!").build();
        }
    }

    private boolean isPaymentMethodExists(Integer id) {
        PaymentMethod paymentMethod = getRepository().findOne(id);
        return paymentMethod != null;
    }

}
