package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.repository.UserRepository;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class UserService {
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Autowired private UserRepository userRepository;

    public UserRepository getRepository() {
        return userRepository;
    }

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> getAllWithRoleName(String name) {
        if (StringUtils.isNotEmpty(name))
            return userRepository.findByUserRole_NameContainsIgnoreCase(name);
        else
            return null;
    }

    public User getById(int id) {
        return userRepository.findOne(id);
    }

    public Page<User> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        boolean unsorted = false;
        Sort.Direction direction;

        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (StringUtils.isEmpty(sortBy))
            unsorted = true;

        Page<User> result;
        if (!unsorted) {
            direction = SearchUtil.determineSortDirection(sortDirection);

            result = getRepository().findAll(new PageRequest(page, size, direction, sortBy));
        }
        else
            result = getRepository().findAll(new PageRequest(page, size));

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

        return SearchUtil.combine4(results);
    }
}
