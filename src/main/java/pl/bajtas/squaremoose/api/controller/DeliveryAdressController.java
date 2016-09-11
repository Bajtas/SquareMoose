package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.DeliveryAdress;
import pl.bajtas.squaremoose.api.service.CategoryService;
import pl.bajtas.squaremoose.api.service.DeliveryAdressService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/DeliveryAdressService")
public class DeliveryAdressController {
    @Autowired
    DeliveryAdressService deliveryAdressService; // Product service bean for connection between controller and service layers
    // Search by Product properties

    /* Main method
    * Takes 0 parameters
    *
    * Returns list of all products with and without categories
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deliveryadresses")
    public Iterable<DeliveryAdress> getAll() {
        return deliveryAdressService.getAll();
    }
}
