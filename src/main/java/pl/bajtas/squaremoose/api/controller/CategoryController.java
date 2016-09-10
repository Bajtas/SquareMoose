package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.service.CategoryService;
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

    @Autowired  CategoryService categoryService; // Category service bean for connection between controller and service layers

    // Read

    //region Description
    /* Main method
    * Takes 0 parameters
    *
    * Returns list of all categories
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/categories")
    public Iterable<Category> getAll() {
        return categoryService.getAll();
    }

    //region Description
    /* Search by Id
    * Takes 1 parameter - Id
    *
    * Returns one Category related to this Id
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/id/{id}")
    public Category getById(@NotNull @PathParam("id") Integer id) {
        return categoryService.getById(id);
    }

    //region Description
    /* Search by Name
    * Takes 1 parameter - Name
    *
    * Returns one Category related to this Name
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/name/{name}")
    public List<Category> getByName(@NotNull @PathParam("name") String name) {
        return categoryService.getByName(name);
    }

    // Add and Update

    //region Description
    /* Add new category to DB
    * Takes 1 parameter - Category in JSON format
    *
    * Returns info
    *
    * If not succeed, returns info about error.
    * */
    //endregion
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/category/add")
    public String addCategory(Category category) {
        return categoryService.addOrUpdate(category, false); // true for add new
    }

    //region Description
    /* Update category in DB
    * Takes 1 parameter - Category in JSON format
    *
    * Returns info
    *
    * If not succeed, returns info about error.
    * */
    //endregion
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/category/update")
    public String updateCategory(Category category) {
        return categoryService.addOrUpdate(category, true); // false for update old
    }

    // Delete

    //region Description
    /* Deletes category by Id or Name
    * Takes 2 parameters
    *
    * Returns info
    *
    * If not succeed, returns info about error.
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/delete")
    public String deleteByIdOrName(@QueryParam("id") Integer id, @QueryParam("name") String name) {
        return categoryService.deleteByIdOrName(id, name);
    }

    // Other

    //region Description
    /* Show stats for all categories
    * Takes 2 parameters
    *
    * Returns stats for every category
    *
    * If all parameters are null, method will Excepction
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/stats")
    public List<CategoryStats> getCategoryStats(@QueryParam("byId") boolean byId, @QueryParam("byName") boolean byName) throws Exception {
        return categoryService.getCategoryStats(byId, byName);
    }
}
