package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.OrderItem;
import pl.bajtas.squaremoose.api.service.OrderItemService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/OrderItemService")
public class OrderItemController {

    @Autowired private OrderItemService orderItemService;

    private OrderItemService getService() {
        return orderItemService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orderitems")
    public Iterable<OrderItem> getAll() {
        return getService().getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orderitems/page/{number}")
    public Page<OrderItem> getPage(@PathParam("number") Integer page,
                                   @QueryParam("size") Integer size,
                                   @QueryParam("sortBy") String sortBy,
                                   @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orderitem/{id}")
    public OrderItem getById(@NotNull @PathParam("id") Integer id) {
        return getService().getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orderitem/order/{id}")
    public List<OrderItem> getByOrderId(@NotNull @PathParam("id") Integer id) {
        return getService().getByOrderId(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orderitem/user/{id}")
    public List<OrderItem> getByUserId(@NotNull @PathParam("id") Integer id) {
        return getService().getByUserId(id);
    }

    // Add new
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/orderitem/add")
    public Response add(OrderItem orderItem) {
        return getService().add(orderItem);
    }

    // Add new to Order
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/orderitem/order/{id}/add")
    public Response addToOrderWithId(@NotNull @PathParam("id") int id, OrderItem orderItem) {
        return getService().addToOrderWithId(id, orderItem);
    }

    // Update
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/orderitem/{id}/update")
    public Response add(@NotNull @PathParam("id") int id, OrderItem orderItem) {
        return getService().update(id, orderItem);
    }

    // Delete
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/orderitem/{id}/delete")
    public Response add(@NotNull @PathParam("id") int id) {
        return getService().delete(id);
    }
}
