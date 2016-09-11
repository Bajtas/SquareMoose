package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.domain.UserRole;
import pl.bajtas.squaremoose.api.repository.UserRepository;
import pl.bajtas.squaremoose.api.repository.UserRoleRepository;
import pl.bajtas.squaremoose.api.util.search.Combiner;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class UserService {
    private static final Logger LOG = Logger.getLogger(UserService.class);
    private static final String DEFAULT_USER_ROLE = "User";

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

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
        } else {
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

    public String add(User user) {
        String login = user.getLogin();
        String email = user.getEmail();
        String password = user.getPassword();

        if (StringUtils.isNotEmpty(login) && StringUtils.isNotEmpty(email)) {
            User loginResult = getRepository().findByLogin(login);
            User emailResult = getRepository().findByEmail(email);

            if (loginResult != null) {
                return "User with same login exist in system, please choose another login.";
            } else if (emailResult != null) {
                return "User with same email exist in system, please choose another email.";
            }

            Base64.Decoder decoder = Base64.getDecoder();
            String decodedPassword = new String(decoder.decode(password), StandardCharsets.UTF_8);

            if (decodedPassword.length() >= 6) {
                String hashedPassword = passwordEncoder.encode(decodedPassword);

                user.setAddedOn(new Date());
                user.setLmod(new Date());
                user.setPassword(hashedPassword);

                UserRole roleForNewUser = userRoleRepository.findByName(DEFAULT_USER_ROLE);
                user.setUserRole(roleForNewUser);

                getRepository().save(user);
                return "Registered successfully!";
            } else {
                return "Password length is to small. Min. 6 characters.";
            }
        } else if (StringUtils.isNotEmpty(login)) {
            return "Email has been not specified!";
        } else if (StringUtils.isNotEmpty(email)) {
            return "Email has been not specified!";
        } else {
            return "Email and login has been not specified!";
        }
    }

    public String update(User newUser) {
        String login = newUser.getLogin(); // get login to find user for update
        String newEmail = newUser.getEmail();
        String newPassword = newUser.getPassword();

        if (StringUtils.isNotEmpty(login)) {
            User old = getRepository().findByLogin(login);

            if (old == null) {
                return "User with specified login not found!";
            }

            if (StringUtils.isNotEmpty(newEmail)) {
                if (getRepository().findByEmail(newEmail) != null) {
                    return "This email is already taken!";
                } else {
                    old.setEmail(newEmail);
                }
            }

            if (StringUtils.isNotEmpty(newPassword)) {
                Base64.Decoder decoder = Base64.getDecoder();
                String decodedPassword = new String(decoder.decode(newPassword), StandardCharsets.UTF_8);

                if (decodedPassword.length() >= 6) {
                    String hashedPassword = passwordEncoder.encode(decodedPassword);
                    old.setPassword(hashedPassword);
                } else {
                    return "Password length is to small. Min. 6 characters.";
                }
            }

            old.setLmod(new Date());
            getRepository().save(old);

            return "Updated successfully.";
        } else {
            return "Login has been not specified!";
        }
    }

    public String delete(int id, User user) {
        String login = user.getLogin();
        String email = user.getEmail();
        String password = user.getPassword();

        if (StringUtils.isNotEmpty(login) && StringUtils.isNotEmpty(email)) {
            User loginResult = getRepository().findByLogin(login);
            User emailResult = getRepository().findByEmail(email);

            if (loginResult == null) {
                return "User with specified email does not exist in system.";
            } else if (emailResult == null) {
                return "User with specified login does not exist in system.";
            }

            Base64.Decoder decoder = Base64.getDecoder();
            String decodedPassword = new String(decoder.decode(password), StandardCharsets.UTF_8);

            if (passwordEncoder.matches(decodedPassword, loginResult.getPassword())) {
                getRepository().delete(loginResult);
                return "Removed successfully!";
            } else {
                return "Passwords doesn't matches.";
            }
        } else if (StringUtils.isNotEmpty(login)) {
            return "Email has been not specified!";
        } else if (StringUtils.isNotEmpty(email)) {
            return "Email has been not specified!";
        } else {
            return "Email and login has been not specified!";
        }
    }
}
