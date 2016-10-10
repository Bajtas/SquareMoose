package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.domain.UserRole;
import pl.bajtas.squaremoose.api.repository.UserRepository;
import pl.bajtas.squaremoose.api.repository.UserRoleRepository;
import pl.bajtas.squaremoose.api.util.search.Combiner;
import pl.bajtas.squaremoose.api.util.search.PageUtil;

import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */

@Service
public class UserService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    private static final String DEFAULT_USER_ROLE = "User";

    // Needs to be lazy, configuration needs to be done first
    @Autowired @Lazy private BCryptPasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;
    @Autowired private UserRoleRepository userRoleRepository;

    public UserRepository getRepository() {
        return userRepository;
    }

    // Events
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    // Search by User properties
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
        PageUtil<User> util = new PageUtil<>();
        return util.getPage(page, size, sortBy, sortDirection, getRepository());
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

     /* --------------------------------------------------------------------------------------------- */

    // Add new
    public Response add(User user) {
        String login = user.getLogin();
        String email = user.getEmail();
        String password = user.getPassword();

        if (StringUtils.isNotEmpty(login) && StringUtils.isNotEmpty(email)) {
            User loginResult = getRepository().findByLogin(login);
            User emailResult = getRepository().findByEmail(email);

            if (loginResult != null) {
                return Response.status(Response.Status.CONFLICT).entity("User with same login exist in system, please choose another login.").build();
            } else if (emailResult != null) {
                return Response.status(Response.Status.CONFLICT).entity("User with same email exist in system, please choose another email.").build();
            }

            if (password.length() >= 6) {
                String hashedPassword = passwordEncoder.encode(password);

                user.setAddedOn(new Date());
                user.setLmod(new Date());
                user.setPassword(hashedPassword);

                UserRole roleForNewUser = userRoleRepository.findByName(DEFAULT_USER_ROLE);
                user.setUserRole(roleForNewUser);

                getRepository().save(user);
                return Response.status(Response.Status.OK).entity("Registered successfully!").build();
            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Password length is to small. Min. 6 characters.").build();
            }
        } else if (StringUtils.isNotEmpty(login)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Login has been not specified!").build();
        } else if (StringUtils.isNotEmpty(email)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email has been not specified!").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("Email and login has been not specified!").build();
    }

    // Update existing
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

    // Delete
    /* TO DO: REMOVE SECOND PARAMETER, CREDENTIALS NEEDS TO BE CHECK THROUGH SPRING SECURITY */
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
