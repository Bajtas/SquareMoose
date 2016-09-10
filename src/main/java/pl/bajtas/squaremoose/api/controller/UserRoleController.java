package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.domain.UserRole;
import pl.bajtas.squaremoose.api.service.UserRoleService;
import pl.bajtas.squaremoose.api.service.UserService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/UserRoleService")
public class UserRoleController {
    @Autowired
    UserRoleService userRoleService; // UserRole service bean for connection between controller and service layers
    // Search by UserRole properties

    //region Description
    /* Main method
    * Takes 0 parameters
    *
    * Returns list of all roles defined in system
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles")
    public Iterable<UserRole> getAll() {
        return userRoleService.getAll();
    }

    //region Description
    /* Main method
    * Takes 1 parameters - Role Id
    *
    * Returns one Role assigned to this Id
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/id/{id}")
    public UserRole getById(@NotNull @PathParam("id") Integer id) {
        return userRoleService.getById(id);
    }

    //region Description
    /* Main method
    * Takes 1 parameters - Role Name
    *
    * Returns one Role assigned to this Name
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/name/{name}")
    public UserRole getById(@NotNull @PathParam("name") String name) {
        return userRoleService.getByName(name);
    }

    //region Description
    /* Main method
    * Takes 1 parameters - Role Name
    *
    * Returns one Role assigned to this Name
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/search")
    public List<UserRole> getByAllProperties(@QueryParam("id") Integer id,
                                             @QueryParam("name") String name,
                                             @QueryParam("username") String username) {
        return userRoleService.searchRole(id, name, username);
    }


}
