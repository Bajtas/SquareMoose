package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import pl.bajtas.squaremoose.api.domain.DeliveryAdress;
import pl.bajtas.squaremoose.api.service.DeliveryAdressService;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/DeliveryAdressService")
@Produces(MediaType.APPLICATION_JSON)
public class DeliveryAddressResource {
    @Autowired
    private DeliveryAdressService deliveryAdressService; // Product service bean for connection between controller and service layers

    private DeliveryAdressService getService() {
        return deliveryAdressService;
    }

    // Search by DeliveryAdress properties

    @GET
    @Path("/deliveryadresses")
    @Transactional
    public Iterable<DeliveryAdress> getAll() {
        return getService().getAll();
    }

    @GET
    @Path("/deliveryadresses/page/{number}")
    @Transactional
    public Page<DeliveryAdress> getPage(@PathParam("number") Integer page,
                                        @QueryParam("size") Integer size,
                                        @QueryParam("sortBy") String sortBy,
                                        @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    @GET
    @Path("/deliveryadress/{id}")
    @Transactional
    public DeliveryAdress getById(@NotNull @PathParam("id") Integer id) {
        return getService().getById(id);
    }

    @GET
    @Path("/deliveryadress/name/{name}")
    @Transactional
    public List<DeliveryAdress> getByNameContainsIgnoreCase(@NotNull @PathParam("name") String name) {
        return getService().getByNameContainsIgnoreCase(name);
    }

    @GET
    @Path("/deliveryadress/surname/{surname}")
    public List<DeliveryAdress> getBySurnameContainsIgnoreCase(@NotNull @PathParam("surname") String surname) {
        return getService().getBySurnameContainsIgnoreCase(surname);
    }

    @GET
    @Path("/deliveryadress/adress/{adress}")
    public List<DeliveryAdress> getByAdressContainsIgnoreCase(@NotNull @PathParam("adress") String adress) {
        return getService().getByAdressContainsIgnoreCase(adress);
    }

    @GET
    @Path("/deliveryadress/town/{town}")
    public List<DeliveryAdress> getByTownContainsIgnoreCase(@NotNull @PathParam("town") String town) {
        return getService().getByTownContainsIgnoreCase(town);
    }

    @GET
    @Path("/deliveryadress/zipcode/{zipcode}")
    public List<DeliveryAdress> getByZipCodeContainsIgnoreCase(@NotNull @PathParam("zipcode") String zipcode) {
        return getService().getByZipCodeContainsIgnoreCase(zipcode);
    }

    @GET
    @Path("/deliveryadress/phone/{phone}")
    public List<DeliveryAdress> getByContactPhoneContainsIgnoreCase(@NotNull @PathParam("phone") String phone) {
        return getService().getByContactPhoneContainsIgnoreCase(phone);
    }

    /* --------------------------------------------------------------------------------------------- */

    /* Find DeliveryAdress by Order properties */

    @GET
    @Path("/deliveryadress/ordered/page/{number}")
    public Page<DeliveryAdress> getByAdressWithOrderNotNull(@PathParam("number") Integer page,
                                                            @QueryParam("size") Integer size,
                                                            @QueryParam("sortBy") String sortBy,
                                                            @QueryParam("dir") String direction) {
        return getService().getByAdressWithOrderNotNull(page, size, sortBy, direction);
    }

    @GET
    @Path("/deliveryadress/unordered/page/{number}")
    public Page<DeliveryAdress> getByAdressWithOrderNull(@PathParam("number") Integer page,
                                                         @QueryParam("size") Integer size,
                                                         @QueryParam("sortBy") String sortBy,
                                                         @QueryParam("dir") String direction) {
        return getService().getByAdressWithOrderNull(page, size, sortBy, direction);
    }

    @GET
    @Path("/deliveryadress/order/{id}")
    public List<DeliveryAdress> getByOrderId(@NotNull @PathParam("id") int id) {
        return getService().getByOrderId(id);
    }

    @GET
    @Path("/deliveryadress/user/{id}")
    public List<DeliveryAdress> getByUserId(@NotNull @PathParam("id") int id) {
        return getService().getByUserId(id);
    }

    @GET
    @Path("/deliveryadress/user/login/{login}")
    @PermitAll
    @Transactional
    public List<DeliveryAdress> getByUserLogin(@NotNull @PathParam("login") String login) {
        return getService().getByUserLogin(login);
    }

    @GET
    @Path("/deliveryadress/user/email/{email}")
    public List<DeliveryAdress> getByUserEmail(@NotNull @PathParam("email") String email) {
        return getService().getByUserEmail(email);
    }

   /* --------------------------------------------------------------------------------------------- */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/deliveryadress/add")
    public Response add(DeliveryAdress deliveryAdress) {
        return getService().add(deliveryAdress);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/deliveryadress/add/{login}")
    public Response add(@NotNull @PathParam("login") String login, DeliveryAdress deliveryAdress) {
        return getService().add(login, deliveryAdress);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/deliveryadress/{id}/update")
    @PermitAll
    public Response add(@NotNull @PathParam("id") int id, DeliveryAdress deliveryAdress) {
        return getService().update(id, deliveryAdress);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/deliveryadress/{id}/delete")
    public Response add(@NotNull @PathParam("id") int id) {
        return getService().delete(id);
    }
}
