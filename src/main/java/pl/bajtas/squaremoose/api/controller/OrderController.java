package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.service.CategoryService;
import pl.bajtas.squaremoose.api.service.OrderService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/OrderService")
public class OrderController {

    @Autowired private OrderService orderService;

    private OrderService getService() {
        return orderService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orders")
    public Iterable<Order> getAll() {
        return getService().getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orders/page/{number}")
    public Page<Order> getPage(@PathParam("number") Integer page,
                                 @QueryParam("size") Integer size,
                                 @QueryParam("sortBy") String sortBy,
                                 @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/order/{id}")
    public Order getById(@NotNull @PathParam("id") Integer id) {
        return getService().getById(id);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/order/user/id/{id}")
    public Order getByUserId(@NotNull @PathParam("id") int id) {
        return getService().getByUserId(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/order/user/login/{login}")
    public Order getByUserLogin(@NotNull @PathParam("login") String login) {
        return getService().getByUserLogin(login);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/order/deliveryadress/id/{id}")
    public Order getByDeliveryAdressId(@NotNull @PathParam("id") Integer id) {
        return getService().getByDeliveryAdressId(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/order/deliverytype/id/{id}")
    public Order getByDeliveryTypeId(@NotNull @PathParam("id") Integer id) {
        return getService().getByDeliveryTypeId(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/order/paymentmethod/id/{id}")
    public Order getByPaymentMethodId(@NotNull @PathParam("id") Integer id) {
        return getService().getByPaymentMethodId(id);
    }
}
