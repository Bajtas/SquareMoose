package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.DeliveryType;
import pl.bajtas.squaremoose.api.domain.OrderItem;
import pl.bajtas.squaremoose.api.service.DeliveryTypeService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Bajtas on 18.09.2016.
 */

@Controller
@Path("/DeliveryTypeService")
public class DeliveryTypeController {

    @Autowired private DeliveryTypeService deliveryTypeService;

    private DeliveryTypeService getService() {
        return deliveryTypeService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deliverytypes")
    public Iterable<DeliveryType> getAll() {
        return getService().getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deliverytypes/page/{number}")
    public Page<DeliveryType> getPage(@PathParam("number") Integer page,
                                      @QueryParam("size") Integer size,
                                      @QueryParam("sortBy") String sortBy,
                                      @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deliverytypes/{id}")
    public DeliveryType getById(@NotNull @PathParam("id") Integer id) {
        return getService().getById(id);
    }


}
