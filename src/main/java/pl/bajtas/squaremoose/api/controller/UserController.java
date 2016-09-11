package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
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

    // Search by User properties

    /* Main method
    * Takes 0 parameters
    *
    * Returns list of users
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public Iterable<User> getAll() {
        return getService().getAll();
    }

    /* getByAllWithRoleName(name)
    * Takes 1 parameter - Role Name
    *
    * Returns users related to Role Name or nothing
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users/role/{name}")
    public List<User> getByAllWithRoleName(@NotNull @PathParam("name") String name) {
        return getService().getAllWithRoleName(name);
    }

    /* getPage(...)
    * Takes 1 path parameter - Number of page
    * Takes 3 path parameters:
    *       - size - number of elements in page
    *       - sortBy - elements which results will be sorted by
    *       - dir - direction of sorting
    *
    * Returns list of all users on this page
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users/page/{number}")
    public Page<User> getPage(@PathParam("number") Integer page,
                              @QueryParam("size") Integer size,
                              @QueryParam("sortBy") String sortBy,
                              @QueryParam("dir") String direction) {
        return getService().getAll(page, size, sortBy, direction);
    }

    /* getById(id)
    * Takes 1 parameter - Id
    *
    * Returns user related to id or nothing
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/id/{id}")
    public User getById(@NotNull @PathParam("id") int id) {
        return getService().getById(id);
    }

    /* getByAllProperties(...)
    * Takes 4 parameters:
    *  - login
    *  - email
    *  - role
    *  - online
    *
    * Returns user related to this search properties or nothing
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users/search")
    public List<User> getByAllProperties(
            @QueryParam("login") String login,
            @QueryParam("email") String email,
            @QueryParam("role") String role,
            @QueryParam("online") Boolean online) {
        return getService().searchUser(login, email, role, online);
    }

    // Add new User

    /*  Add new User to DB
    * Takes 1 parameter : User object
    *
    * Returns info with error or success if registration is successful.
    * */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    public String register(User user) {
        return getService().register(user);
    }

    /*  Add new User to DB
    * Takes 1 parameter : User object
    *
    * Returns info with error or success if remove is successful.
    * */
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/update")
    public String update(User user) {
        return getService().update(user);
    }

    /*  Add new User to DB
    * Takes 1 parameter : User object
    *
    * Returns info with error or success if remove is successful.
    * */
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete")
    public String delete(User user) {
        return getService().delete(user);
    }
}
