package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.PaymentMethod;
import pl.bajtas.squaremoose.api.service.PaymentMethodService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/PaymentMethodService")
public class PaymentMethodController {

    @Autowired
    PaymentMethodService paymentMethodService;

    private PaymentMethodService getService() {
        return paymentMethodService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/methods")
    @PermitAll
    public Iterable<PaymentMethod> getAll() {
        return paymentMethodService.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/methods/page/{number}")
    @PermitAll
    public Page<PaymentMethod> getPage(@PathParam("number") Integer page,
                                       @QueryParam("size") Integer size,
                                       @QueryParam("sortBy") String sortBy,
                                       @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/method/{id}")
    public PaymentMethod getById(@NotNull @PathParam("id") Integer id) {
        return getService().getById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/method/add")
    public Response add(PaymentMethod paymentMethod) {
        return getService().add(paymentMethod);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/method/{id}/update")
    @RolesAllowed("Admin")
    public Response update(@NotNull @PathParam("id") int id, PaymentMethod paymentMethod) {
        return getService().update(id, paymentMethod);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/method/{id}/delete")
    public Response delete(@NotNull @PathParam("id") Integer id) {
        return getService().delete(id);
    }
}
