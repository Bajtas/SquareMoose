package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.OrderStateHistory;
import pl.bajtas.squaremoose.api.service.OrderStateHistoryService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/OrderStateHistoryService")
public class OrderStateHistoryController {
    @Autowired
    OrderStateHistoryService orderStateHistoryService; // Product service bean for connection between controller and service layers

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/histories")
    public Iterable<OrderStateHistory> getAll() {
        return orderStateHistoryService.getAll();
    }


    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/history/{id}")
    public Response delete(@NotNull @PathParam("id") int id) {
        return orderStateHistoryService.delete(id);
    }
}
