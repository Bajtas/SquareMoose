package pl.bajtas.squaremoose.api.controller;

/**
 * Created by Bajtas on 18.09.2016.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.OrderState;
import pl.bajtas.squaremoose.api.service.OrderStateService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/OrderStateService")
public class OrderStateController {
    @Autowired
    private OrderStateService orderStateService; // ActualOrderStateService service bean for connection between controller and service layers

    private OrderStateService getService() {
        return  orderStateService;
    }

    // Search by ActualOrderStateService properties
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/states")
    public Iterable<OrderState> getAll() {
        return getService().getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/states/page/{number}")
    public Page<OrderState> getPage(@PathParam("number") Integer page,
                                          @QueryParam("size") Integer size,
                                          @QueryParam("sortBy") String sortBy,
                                          @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/state/{id}")
    public OrderState getById(@NotNull @PathParam("id") Integer id) {
        return getService().getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/states/name/{name}")
    public List<OrderState> getByNameContainsIgnoreCase(@NotNull @PathParam("name") String name) {
        return getService().getByNameContainsIgnoreCase(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/states/description/{description}")
    public List<OrderState> getByDescriptionContainsIgnoreCase(@NotNull @PathParam("description") String description) {
        return getService().getByDescriptionContainsIgnoreCase(description);
    }
    /* --------------------------------------------------------------------------------------------- */

//    /* Find by Order properties */
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/state/order/{id}")
//    public ActualOrderState getByOrderId(@NotNull @PathParam("id") int id) {
//        return getService().getByOrderId(id);
//    }


    // Add new
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/state/add")
    public String add(OrderState orderState) {
        return getService().add(orderState);
    }

    // Update
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/state/{id}/update")
    public String add(@NotNull @PathParam("id") int id, OrderState orderState) {
        return getService().update(id, orderState);
    }

    // Delete
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/state/{id}/delete")
    public String add(@NotNull @PathParam("id") int id) {
        return getService().delete(id);
    }

}
