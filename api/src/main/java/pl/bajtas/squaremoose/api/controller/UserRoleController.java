package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.UserRole;
import pl.bajtas.squaremoose.api.service.UserRoleService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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

    /* Main method
    * Takes 0 parameters
    *
    * Returns list of all roles defined in system
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles")
    @PermitAll
    public Iterable<UserRole> getAll() {
        return userRoleService.getAll();
    }

    /* Main method
    * Takes 1 parameters - Role Id
    *
    * Returns one Role assigned to this Id
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/id/{id}")
    public UserRole getById(@NotNull @PathParam("id") Integer id) {
        return userRoleService.getById(id);
    }

    /* Main method
    * Takes 1 parameters - Role Name
    *
    * Returns one Role assigned to this Name
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/name/{name}")
    public UserRole getById(@NotNull @PathParam("name") String name) {
        return userRoleService.getByName(name);
    }

    /* Main method
    * Takes 1 parameters - Role Name
    *
    * Returns one Role assigned to this Name
    * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/roles/search")
    public List<UserRole> getByAllProperties(@QueryParam("id") Integer id,
                                             @QueryParam("name") String name,
                                             @QueryParam("username") String username) {
        return userRoleService.searchRole(id, name, username);
    }

    /* Main method
    * Takes 1 parameters - Role Name
    *
    * Returns one Role assigned to this Name
    * */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/roles/add")
    public String add(UserRole newRole) {
        return userRoleService.add(newRole);
    }

    /* Main method
    * Takes 1 parameters - Role Name
    *
    * Returns one Role assigned to this Name
    * */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/role/{id}/update")
    public String add(@NotNull @PathParam("id") int id, UserRole newRole) {
        return userRoleService.update(id, newRole);
    }

    /* Main method
    * Takes 1 parameters - Role Name
    *
    * Returns one Role assigned to this Name
    * */
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/role/{id}/delete")
    public String add(@NotNull @PathParam("id") int id) {
        return userRoleService.delete(id);
    }
}
