package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.UserRole;
import pl.bajtas.squaremoose.api.repository.UserRoleRepository;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class UserRoleService implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Autowired
    private UserRoleRepository userRoleRepository;

    // Events
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Insert default roles to DB if they does not exist.
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

    public UserRoleRepository getRepository() {
        return userRoleRepository;
    }

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

    public List<UserRole> getByNameContains(String name) {
        if (name != null) {
            return getRepository().findByNameContainsIgnoreCase(name); // findByNameContainsIgnoreCase
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
            results.add(getByNameContains(name));
        }

        if (username != null) {
            results.add(getByUsername(name));
        }

        return SearchUtil.combine2(results);
    }

    private List<UserRole> getByUsername(String name) {
        return getRepository().findByUsers_LoginContainsIgnoreCase(name);
    }
}
