package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.UserRole;
import pl.bajtas.squaremoose.api.repository.UserRoleRepository;
import pl.bajtas.squaremoose.api.util.search.Combiner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */

@Service
public class UserRoleService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = Logger.getLogger(UserRoleService.class);

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRoleRepository getRepository() {
        return userRoleRepository;
    }

    // Events
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Insert default roles to DB if they does not exist.
        LOG.info("Check if there are proper roles defined in DB.");
        String roleNames[] = {"Admin", "Moderator", "User", "Guest"};
        for (String name : roleNames) {
            if (getRepository().findByName(name) == null) {
                UserRole role = new UserRole();
                role.setName(name);
                role.setLmod(new Date());

                getRepository().save(role);
            }
        }
    }

    // Search by User Role properties
    public Iterable<UserRole> getAll() {
        return getRepository().findAll();
    }

    public UserRole getById(Integer id) {
        LOG.warn("getById() - Id: " + id);
        if (id != null) {
            return getRepository().findOne(id);
        } else {
            LOG.warn("Search has not been performed - Id is null!");
            return null;
        }
    }

    public UserRole getByName(String name) {
        if (name != null) {
            return getRepository().findByName(name);
        } else {
            LOG.warn("Search has not been performed - Name is null!");
            return null;
        }
    }

    public List<UserRole> getByNameContainsIgnoreCase(String name) {
        if (name != null) {
            return getRepository().findByNameContainsIgnoreCase(name);
        } else {
            LOG.warn("Search has not been performed - Name is null!");
            return null;
        }
    }

    public List<UserRole> searchRole(Integer id, String name, String username) {
        if (id == null && name == null && username == null) {
            Iterable<UserRole> source = getAll();
            List<UserRole> allRoles = new ArrayList<>();
            source.forEach(allRoles::add);
            return allRoles;
        }

        List<List<UserRole>> results = new ArrayList<>();

        if (name != null) {
            results.add(getByNameContainsIgnoreCase(name));
        }

        if (username != null) {
            results.add(getByUsername(name));
        }

        Combiner<UserRole> combiner = new Combiner<>(results);
        return combiner.combine();
    }

    /* --------------------------------------------------------------------------------------------- */

    // Search by User properties
    private List<UserRole> getByUsername(String name) {
        return getRepository().findByUsers_LoginContainsIgnoreCase(name);
    }

    /* --------------------------------------------------------------------------------------------- */

    // Add new role to DB
    public String add(UserRole newRole) {
        String newRoleName = newRole.getName();

        if (StringUtils.isNotEmpty(newRole.getName())) {
            UserRole old = getRepository().findByName(newRoleName);
            if (old != null) {
                return "Role with given name already exist.";
            } else {
                newRole.setLmod(new Date());
                getRepository().save(newRole);
                return "New user role added successfully.";
            }
        }

        return "Specified role name is empty.";
    }

    // Update existing
    public String update(int id, UserRole newRole) {
        UserRole old = getRepository().findOne(id);
        if (old != null) {
            newRole.setId(id);
            newRole.setLmod(new Date());

            getRepository().save(newRole);
            return "User role with id: " + id + "updated successfully!";
        } else {
            return "User role with id: " + id + " not found!";
        }
    }

    // Delete
    public String delete(int id) {
        getRepository().delete(id);

        return "Role with id: " + id + " deleted.";
    }

}
