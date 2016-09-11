package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.repository.UserRepository;
import pl.bajtas.squaremoose.api.util.search.Combiner;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class UserService {
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public UserRepository getRepository() {
        return userRepository;
    }

    public Iterable<User> getAll() {
        LOG.info("Getting all users from DB.");
        Iterable<User> results = getRepository().findAll();
        LOG.info("End of getAll() method of User Service");

        return results;
    }

    public List<User> getAllWithRoleName(String name) {
        LOG.info("Getting all users with role name: " + name);
        if (StringUtils.isNotEmpty(name)) {
            return getRepository().findByUserRole_NameContainsIgnoreCase(name);
        }
        else {
            LOG.info("Name of role is not specified. Method getAllWithRoleName(...) will return null.");
            return null;
        }
    }

    public User getById(int id) {
        LOG.info("Getting user assigned to id: " + id);
        return getRepository().findOne(id);
    }

    public Page<User> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        LOG.info("Getting all users wrapped with pages.");
        boolean unsorted = false;
        Sort.Direction direction;

        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (StringUtils.isEmpty(sortBy))
            unsorted = true;

        LOG.info("Page number: " + page + " size of page: " + size);


        Page<User> result;
        if (!unsorted) {
            direction = SearchUtil.determineSortDirection(sortDirection);

            LOG.info("Results will be sorted by: " + sortBy + " With sorting direction: " + sortDirection);
            result = getRepository().findAll(new PageRequest(page, size, direction, sortBy));
        } else {
            LOG.info("Unsorted results.");
            result = getRepository().findAll(new PageRequest(page, size));
        }

        LOG.info("Exiting getAll(...) of User Service");
        return result;
    }

    public List<User> searchUser(String login, String email, String role, Boolean online) {
        List<List<User>> results = new ArrayList<>();

        if (StringUtils.isNotEmpty(login))
            results.add(getRepository().findByLoginStartingWithIgnoreCase(login));
        if (StringUtils.isNotEmpty(email))
            results.add(getRepository().findByEmailContainsIgnoreCase(email));
        if (StringUtils.isNotEmpty(role))
            results.add(getRepository().findByUserRole_NameContainsIgnoreCase(role));
        if (online != null)
            results.add(getRepository().findByIsOnline(online));

        Combiner<User> combiner = new Combiner<>(results);
        return combiner.combine();
    }
}
