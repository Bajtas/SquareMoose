package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.service.CategoryService;
import pl.bajtas.squaremoose.api.service.ProductService;
import pl.bajtas.squaremoose.api.util.search.CategoryStats;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */

@Controller
@Path("/CategoryService")
public class CategoryController {

    @Autowired  CategoryService categoryService; // Product service bean for connection between controller and service layers
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
    @Path("/categories")
    public Iterable<Category> getAll() {
        return categoryService.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/id/{id}")
    public Category getById(@NotNull @PathParam("id") Integer id) {
        return categoryService.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/name/{name}")
    public List<Category> getByName(@NotNull @PathParam("name") String name) {
        return categoryService.getByName(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/stats")
    public List<CategoryStats> getCategoryStats(@QueryParam("byId") boolean byId, @QueryParam("byName") boolean byName) {
        return categoryService.getCategoryStats(byId, byName);
    }
}
