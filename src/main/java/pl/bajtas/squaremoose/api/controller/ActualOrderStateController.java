package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.service.ActualOrderStateService;
import pl.bajtas.squaremoose.api.service.CategoryService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/ActualOrderStateService")
public class ActualOrderStateController {
    @Autowired
    ActualOrderStateService actualOrderStateService; // Product service bean for connection between controller and service layers
    // Search by Product properties

    //region Description
    /* Main method
    * Takes 0 parameters
    *
    * Returns list of all products with and without categories
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/actualstates")
    public Iterable<ActualOrderState> getAll() {
        return actualOrderStateService.getAll();
    }
}
