package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.DeliveryType;
import pl.bajtas.squaremoose.api.service.DeliveryTypeService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Bajtas on 18.09.2016.
 */

@Controller
@Path("/DeliveryTypeService")
public class DeliveryTypeController {

    @Autowired
    private DeliveryTypeService deliveryTypeService;

    private DeliveryTypeService getService() {
        return deliveryTypeService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deliverytypes")
    @PermitAll
    public Iterable<DeliveryType> getAll() {
        return getService().getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deliverytypes/page/{number}")
    @PermitAll
    public Page<DeliveryType> getPage(@PathParam("number") Integer page,
                                      @QueryParam("size") Integer size,
                                      @QueryParam("sortBy") String sortBy,
                                      @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deliverytypes/{id}")
    @PermitAll
    public DeliveryType getById(@NotNull @PathParam("id") Integer id) {
        return getService().getById(id);
    }


   /* --------------------------------------------------------------------------------------------- */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/deliverytypes/add")
    public Response add(DeliveryType deliveryType) {
        return getService().add(deliveryType);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/deliverytypes/{id}/update")
    public Response update(@NotNull @PathParam("id") int id, DeliveryType deliveryType) {
        return getService().update(id, deliveryType);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/deliverytypes/{id}/delete")
    @RolesAllowed("Admin")
    public Response delete(@NotNull @PathParam("id") int id) {
        return getService().delete(id);
    }

}
