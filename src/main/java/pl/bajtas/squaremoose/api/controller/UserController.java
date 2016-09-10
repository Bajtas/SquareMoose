package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.service.UserService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/UserService")
public class UserController {

    @Autowired
    UserService userService; // Product service bean for connection between controller and service layers

    public UserService getService() {
        return userService;
    }

    // Search by Product properties

    //region Description
    /* Main method
    * Takes 0 parameters
    *
    * Returns list of users
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public Iterable<User> getAll() {
        return getService().getAll();
    }

    //region Description
    /* Main method
    * Takes 1 path parameter - Number of page
    * Takes 3 path parameters:
    *       - size - number of elements in page
    *       - sortBy - elements which results will be sorted by
    *       - dir - direction of sorting
    *
    * Returns list of all users on this page
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users/page/{number}")
    public Page<User> getPage(@PathParam("number") Integer page,
                              @QueryParam("size") Integer size,
                              @QueryParam("sortBy") String sortBy,
                              @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    //region Description
    /* Main method
    * Takes 1 parameter - Id
    *
    * Returns user related to id or nothing
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/id/{id}")
    public User getById(@NotNull @PathParam("id") int id) {
        return getService().getById(id);
    }

    //region Description
    /* Main method
    * Takes 1 parameter - Role Name
    *
    * Returns users related to Role Name or nothing
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users/role/{name}")
    public List<User> getByAllWithRoleName(@NotNull @PathParam("name") String name) {
        return getService().getAllWithRoleName(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users/search")
    public List<User> getByAllProperties (
            @QueryParam("login") String login,
            @QueryParam("email") String email,
            @QueryParam("role") String role,
            @QueryParam("online") Boolean online) throws Exception {
        return getService().searchUser(login, email, role, online);
    }
}
