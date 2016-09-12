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
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/ActualOrderStateService")
public class ActualOrderStateController {
    @Autowired
    ActualOrderStateService actualOrderStateService; // ActualOrderStateService service bean for connection between controller and service layers

    // Search by ActualOrderStateService properties
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates")
    public Iterable<ActualOrderState> getAll() {
        return actualOrderStateService.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates/page/{number}")
    public Page<ActualOrderState> getPage(@PathParam("number") Integer page,
                                 @QueryParam("size") Integer size,
                                 @QueryParam("sortBy") String sortBy,
                                 @QueryParam("dir") String direction) {
        return actualOrderStateService.getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates/{id}")
    public ActualOrderState getById(@NotNull @PathParam("id") Integer id) {
        return actualOrderStateService.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates/name/{name}")
    public List<ActualOrderState> getByNameContainsIgnoreCase(@NotNull @PathParam("name") String name) {
        return actualOrderStateService.getByNameContainsIgnoreCase(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates/description/{description}")
    public List<ActualOrderState> getByDescriptionContainsIgnoreCase(@NotNull @PathParam("description") String description) {
        return actualOrderStateService.getByDescriptionContainsIgnoreCase(description);
    }
    /* --------------------------------------------------------------------------------------------- */

    /* Find by Order properties */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates/order/{id}")
    public ActualOrderState getByOrderId(@NotNull @PathParam("id") int id) {
        return actualOrderStateService.getByOrderId(id);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/actualstates/add")
    public String add(ActualOrderState actualOrderState) {
        return actualOrderStateService.add(actualOrderState);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/actualstate/{id}/update")
    public String add(@NotNull @PathParam("id") int id, ActualOrderState actualOrderState) {
        return actualOrderStateService.update(id, actualOrderState);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/actualstate/{id}/delete")
    public String add(@NotNull @PathParam("id") int id) {
        return actualOrderStateService.delete(id);
    }

}
