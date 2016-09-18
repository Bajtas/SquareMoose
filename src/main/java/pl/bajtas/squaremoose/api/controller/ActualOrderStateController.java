package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.service.ActualOrderStateService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/ActualOrderStateService")
public class ActualOrderStateController {
    @Autowired private ActualOrderStateService actualOrderStateService; // ActualOrderStateService service bean for connection between controller and service layers

    private ActualOrderStateService getService() {
        return  actualOrderStateService;
    }

    // Search by ActualOrderStateService properties
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates")
    public Iterable<ActualOrderState> getAll() {
        return getService().getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates/page/{number}")
    public Page<ActualOrderState> getPage(@PathParam("number") Integer page,
                                 @QueryParam("size") Integer size,
                                 @QueryParam("sortBy") String sortBy,
                                 @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstate/{id}")
    public ActualOrderState getById(@NotNull @PathParam("id") Integer id) {
        return getService().getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates/name/{name}")
    public List<ActualOrderState> getByNameContainsIgnoreCase(@NotNull @PathParam("name") String name) {
        return getService().getByNameContainsIgnoreCase(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates/description/{description}")
    public List<ActualOrderState> getByDescriptionContainsIgnoreCase(@NotNull @PathParam("description") String description) {
        return getService().getByDescriptionContainsIgnoreCase(description);
    }
    /* --------------------------------------------------------------------------------------------- */

    /* Find by Order properties */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstate/order/{id}")
    public ActualOrderState getByOrderId(@NotNull @PathParam("id") int id) {
        return getService().getByOrderId(id);
    }


    // Add new
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/actualstate/add")
    public Response add(ActualOrderState actualOrderState) {
        return getService().add(actualOrderState);
    }

    // Update
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/actualstate/{id}/update")
    public String add(@NotNull @PathParam("id") int id, ActualOrderState actualOrderState) {
        return getService().update(id, actualOrderState);
    }

    // Delete
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/actualstate/{id}/delete")
    public String add(@NotNull @PathParam("id") int id) {
        return getService().delete(id);
    }

}
