package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.service.OrderService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/OrderService")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private OrderService getService() {
        return orderService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orders")
    @PermitAll
    public Iterable<Order> getAll() {
        return getService().getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orders/page/{number}")
    @PermitAll
    public Page<Order> getPage(@PathParam("number") Integer page,
                               @QueryParam("size") Integer size,
                               @QueryParam("sortBy") String sortBy,
                               @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/order/{id}")
    @PermitAll
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
    @Path("/orders/user/login/{login}")
    @PermitAll
    public List<Order> getByUserLogin(@NotNull @PathParam("login") String login) {
        return getService().getByUserLogin(login);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orders/user/login/{login}/order/{id}")
    @PermitAll
    public Order getByUserLogin(@NotNull @PathParam("login") String login, @NotNull @PathParam("id") int id) {
        return getService().getByUserLoginAndOrderId(login, id);
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
    public List<Order> getByPaymentMethodId(@NotNull @PathParam("id") Integer id) {
        return getService().getByPaymentMethodId(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/order/add")
    public Response add(Order order) {
        return getService().add(order);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/order/{id}/update")
    public Response update(@NotNull @PathParam("id") int id, Order updatedOrder) {
        return getService().update(id, updatedOrder); // false for update old
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/order/{id}/delete")
    public Response delete(@NotNull @PathParam("id") Integer id) {
        return getService().delete(id);
    }
}
