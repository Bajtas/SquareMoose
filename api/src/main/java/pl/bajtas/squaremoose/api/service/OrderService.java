package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bajtas.squaremoose.api.domain.*;
import pl.bajtas.squaremoose.api.repository.*;
import pl.bajtas.squaremoose.api.service.generic.GenericService;
import pl.bajtas.squaremoose.api.util.email.EmailFactory;
import pl.bajtas.squaremoose.api.util.search.PageUtil;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static pl.bajtas.squaremoose.api.util.email.EmailFactory.EMAIL_TEMPLATE.ORDER_CREATED;

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
    @Autowired
    private MailService mailService;

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
    @Transactional
    public Page<Order> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        PageUtil<Order> util = new PageUtil<>();
        Page<Order> result = util.getPage(page, size, sortBy, sortDirection, getRepository());
        List<Order> notDistinctResult = result.getContent();

        for (Order order : notDistinctResult) {
            order.setOrderItems(order.getOrderItems().stream().distinct().collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    @Transactional
    public Order getById(int id) {
        Order order = getRepository().findOne(id);
        List<OrderItem> distinctOrderItems = order.getOrderItems().stream().distinct().collect(Collectors.toList());
        order.setOrderItems(distinctOrderItems);
        if (order.getActualOrderState() != null) {
            List<OrderStateHistory> distinctOrderStateHistory = order.getActualOrderState().getOrderStateHistories().stream().distinct().collect(Collectors.toList());
            order.getActualOrderState().setOrderStateHistories(distinctOrderStateHistory);
        }
        return order;
    }

    public Order getByUserId(Integer id) {
        return getRepository().findByUser_Id(id);
    }

    @Transactional
    public List<Order> getByUserLogin(String login) {
        List<Order> result = getRepository().findByUser_Login(login).stream().distinct().collect(Collectors.toList());
        for (Order order : result) {
            if (order.getDeliveryType() != null && order.getDeliveryType().getOrders() != null)
                order.getDeliveryType().getOrders().clear();
            if (order.getPaymentMethod() != null && order.getPaymentMethod().getOrders() != null)
                order.getPaymentMethod().getOrders().clear();
            if (order.getDeliveryAdress() != null && order.getDeliveryAdress().getOrders() != null)
                order.getDeliveryAdress().getOrders().clear();
            if (order.getDeliveryAdress() != null && order.getDeliveryAdress().getUsers() != null)
                order.getDeliveryAdress().getUsers().clear();
            if (order.getUser() != null && order.getUser().getDeliveryAdresses() != null)
                order.getUser().getDeliveryAdresses().clear();
        }
        return result;
    }

    @Transactional
    public Order getByUserLoginAndOrderId(String login, int id) {
        Order result = getRepository().findByIdAndUser_Login(id, login);
        result.setOrderItems(result.getOrderItems().stream().distinct().collect(Collectors.toList()));
        if (result.getDeliveryType() != null && result.getDeliveryType().getOrders() != null)
            result.getDeliveryType().getOrders().clear();
        if (result.getPaymentMethod() != null && result.getPaymentMethod().getOrders() != null)
            result.getPaymentMethod().getOrders().clear();
        if (result.getDeliveryAdress() != null && result.getDeliveryAdress().getOrders() != null)
            result.getDeliveryAdress().getOrders().clear();
        if (result.getDeliveryAdress() != null && result.getDeliveryAdress().getUsers() != null)
            result.getDeliveryAdress().getUsers().clear();
        if (result.getUser() != null && result.getUser().getDeliveryAdresses() != null)
            result.getUser().getDeliveryAdresses().clear();

        result.getOrderItems().forEach(p -> {
            if (p.getProduct() != null && p.getProduct().getOrderItems() != null)
                p.getProduct().getOrderItems().clear();
            if (p.getProduct() != null && p.getProduct().getImages() != null)
                p.getProduct().setImages(p.getProduct().getImages().stream().distinct().collect(Collectors.toList()));
        });
        return result;
    }

    @Override
    @Transactional
    public Response add(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        DeliveryAdress deliveryAdress = order.getDeliveryAdress();
        DeliveryType deliveryType = order.getDeliveryType();
        User user = order.getUser();

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User is not specified please specify user!").build();
        }
        if (user.getId() == null) {
            User guest = userRepository.findByLogin("Guest");
            order.setUser(guest);
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
            float fullPrice = 0;
            for (OrderItem item : order.getOrderItems())
                fullPrice += item.getAmount() * item.getProduct().getPrice();

            order.setFullPrice(fullPrice);

            getRepository().save(order);

            ActualOrderState actualOrderState = new ActualOrderState();
            actualOrderState.setName("Ordered");
            actualOrderState.setLmod(new Date());
            actualOrderState.setDescription(" ");
            actualOrderState.setOrder(order);

            actualOrderStateRepository.save(actualOrderState);

            for (OrderItem item : orderItems) {
                item.setOrder(order);
                orderItemRepository.save(item);
            }

            mailService.sendEmail(new EmailFactory(ORDER_CREATED).mailTo(user.getEmail()).build(order));
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("Order added successfully! Id: " + order.getId()).build();
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
