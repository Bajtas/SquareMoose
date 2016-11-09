package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.*;
import pl.bajtas.squaremoose.api.repository.*;
import pl.bajtas.squaremoose.api.service.generic.GenericService;
import pl.bajtas.squaremoose.api.util.search.PageUtil;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class OrderService implements GenericService<Order, OrderRepository>, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;
    @Autowired
    private DeliveryAdressRepository deliveryAdressRepository;
    @Autowired
    private OrderStateHistoryRepository orderStateHistoryRepository;
    @Autowired
    private ActualOrderStateRepository actualOrderStateRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    @Override
    public OrderRepository getRepository() {
        return orderRepository;
    }

    @Override
    public Iterable<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        PageUtil<Order> util = new PageUtil<>();
        return util.getPage(page, size, sortBy, sortDirection, getRepository());
    }

    @Override
    public Order getById(int id) {
        Order order = getRepository().findDistinctById(id);
        List<OrderItem> distinctOrderItems = order.getOrderItems().stream().distinct().collect(Collectors.toList());
        order.setOrderItems(distinctOrderItems);
        return order;
    }

    public Order getByUserId(Integer id) {
        return getRepository().findByUser_Id(id);
    }

    public List<Order> getByUserLogin(String login) {
        List<Order> result = getRepository().findByUser_Login(login).stream().distinct().collect(Collectors.toList());
        result.forEach(x -> {
            x.getDeliveryType().getOrders().clear();
            x.getPaymentMethod().getOrders().clear();
            x.getDeliveryAdress().getOrders().clear();
            x.getDeliveryAdress().getUsers().clear();
            x.getUser().setUserRole(null);
            x.getUser().getDeliveryAdresses().clear();
        });
        return result;
    }

    public Order getByUserLoginAndOrderId(String login, int id) {
        Order result = getRepository().findByIdAndUser_Login(id, login);
        result.setOrderItems(result.getOrderItems().stream().distinct().collect(Collectors.toList()));
        result.getDeliveryType().getOrders().clear();
        result.getPaymentMethod().getOrders().clear();
        result.getDeliveryAdress().getOrders().clear();
        result.getDeliveryAdress().getUsers().clear();
        result.getUser().setUserRole(null);
        result.getUser().getDeliveryAdresses().clear();
        result.getOrderItems().forEach(p -> {
            p.getProduct().getOrderItems().clear();
            p.getProduct().setImages(p.getProduct().getImages().stream().distinct().collect(Collectors.toList()));
        });
        return result;
    }

    @Override
    public Response add(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        DeliveryAdress deliveryAdress = order.getDeliveryAdress();
        DeliveryType deliveryType = order.getDeliveryType();
        User user = order.getUser();

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User is not specified please specify user!").build();
        }
        if (user.getId() < 1) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User with id: " + user.getId() + " not exist. Please specify user with proper id!").build();
        }

        try {
            if (deliveryType != null) {
                if (deliveryTypeNotExist(deliveryType.getId())) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Delivery type with id: " + deliveryType.getId() + " does not exist!").build();
                }
            }
            if (deliveryAdress != null) {
                deliveryAdressRepository.save(deliveryAdress);
            }

            order.setAddedOn(new Date());
            order.setLmod(new Date());

            ActualOrderState actualOrderState = new ActualOrderState();
            actualOrderState.setName("Ordered");
            actualOrderState.setLmod(new Date());
            actualOrderState.setOrder(order);

            actualOrderStateRepository.save(actualOrderState);

            order.setActualOrderState(actualOrderState);
            getRepository().save(order);

            for (OrderItem item : orderItems) {
                item.setOrder(order);
                orderItemRepository.save(item);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("Product added successfully! Id: " + order.getId()).build();
    }

    @Override
    public Response update(int id, Order order) {
        LOG.info("Trying to save order with id: " + id);

        try {
            Order old = getRepository().findOne(id);
            if (old != null) {
                order.setId(id);

                ActualOrderState actualOrderState = order.getActualOrderState();
                DeliveryAdress deliveryAdress = order.getDeliveryAdress();
                DeliveryType deliveryType = order.getDeliveryType();
                PaymentMethod paymentMethod = order.getPaymentMethod();

                if (actualOrderState != null) {
                    List<OrderStateHistory> actualOrderStateOrderStateHistories;
                    if (actualOrderStateRepository.findOne(actualOrderState.getId()) != null) {
                        actualOrderStateOrderStateHistories = actualOrderState.getOrderStateHistories();

                        // TODO everything
                    } else {
                        return Response.status(Response.Status.BAD_REQUEST).entity("ActualOrderState with given id: " + actualOrderState.getId() + " not found!").build();
                    }
                }

                getRepository().save(order);
                return Response.status(Response.Status.OK).entity("Order with id: " + id + " updated successfully!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Order with given id: " + id + " not found!").build();
            }
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }
    }

    @Override
    public Response delete(int id) {
        LOG.info("Trying to delete Order.");
        String info = "Deleted successfully!";

        LOG.info("Order with id: " + id + " will be deleted.");

        Order order = getRepository().findOne(id);
        if (order != null) {
            try {
                List<OrderItem> orderItems = order.getOrderItems();
                if (orderItems.size() != 0) {
                    for (OrderItem orderItem : orderItems) {
                        orderItemRepository.delete(orderItem);
                    }
                }
                ActualOrderState actualOrderState = order.getActualOrderState();
                if (actualOrderState != null) {
                    actualOrderState.setOrder(null);
                    actualOrderStateRepository.save(actualOrderState);
                }
                User user = order.getUser();
                if (user != null) {
                    List<Order> orders = user.getOrders();
                    if (orders.size() != 0) {
                        orders.remove(order);
                    }
                    user.setOrders(orders);
                    userRepository.save(user);
                }
                getRepository().delete(order);

                return Response.status(Response.Status.OK).entity("Order with id: " + id + " deleted successfully!").build();
            } catch (Exception e) {
                LOG.error("Order with id: " + id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Order with given id: " + id + " not found!").build();
        }
    }

    private boolean deliveryTypeNotExist(Integer id) {
        DeliveryType type = deliveryTypeRepository.findOne(id);
        return type == null;
    }

    public Order getByDeliveryAdressId(Integer id) {
        return getRepository().findByDeliveryAdress_Id(id);
    }

    public Order getByDeliveryTypeId(Integer id) {
        return getRepository().findByDeliveryType_Id(id);
    }

    public List<Order> getByPaymentMethodId(Integer id) {
        return getRepository().findByPaymentMethod_Id(id);
    }
}
