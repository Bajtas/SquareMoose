package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.config.AuthenticationFilter;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.domain.UserRole;
import pl.bajtas.squaremoose.api.repository.UserRepository;
import pl.bajtas.squaremoose.api.repository.UserRoleRepository;
import pl.bajtas.squaremoose.api.util.search.Combiner;
import pl.bajtas.squaremoose.api.util.search.PageUtil;

import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Bajtas on 04.09.2016.
 */

@Service
public class UserService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    private static final String DEFAULT_USER_ROLE = "User";

    private static final String DEFAULT_ADMIN_LOGIN = "admin";
    private static final String DEFAULT_ADMIN_EMAIL = "admin@gmail.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "123";

    // Needs to be lazy, configuration needs to be done first
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

    // Events
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        User admin = getRepository().findByLogin(DEFAULT_ADMIN_LOGIN);
        if (admin == null) {
            admin = new User();
            admin.setLogin(DEFAULT_ADMIN_LOGIN);
            admin.setEmail(DEFAULT_ADMIN_EMAIL);
            admin.setUserRole(userRoleRepository.findByName("Admin"));
            admin.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
            getRepository().save(admin);
        }
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

    public User getByLogin(String login) {
        LOG.info("Getting user with login: " + login);
        return getRepository().findByLogin(login);
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
    public Response update(User newUser, String authorization) {
        //Get encoded username and password
        final String encodedUserPassword = authorization.replaceFirst(AuthenticationFilter.AUTHENTICATION_SCHEME + " ", "");

        //Decode username and password
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
        final String password = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;

        //Is user valid?
        if (!isUserValid(username, password) && (username != null ? username.equals(newUser.getLogin()) : false))
            return Response.status(Response.Status.FORBIDDEN).entity("Access denied!").build();

        String login = newUser.getLogin(); // get login to find user for update
        String newEmail = newUser.getEmail();
        String newPassword = newUser.getPassword();

        if (StringUtils.isNotEmpty(login)) {
            User old = getRepository().findByLogin(login);

            if (old == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("User with specified login not found!").build();
            }

            if (StringUtils.isNotEmpty(newEmail)) {
                if (getRepository().findByEmail(newEmail) != null) {
                    return Response.status(Response.Status.CONFLICT).entity("This email is already taken!").build();
                } else {
                    old.setEmail(newEmail);
                }
            }

            if (StringUtils.isNotEmpty(newPassword)) {
                String decodedPassword = new String(Base64.decode(newPassword.getBytes()), StandardCharsets.UTF_8);

                if (decodedPassword.length() >= 6) {
                    String hashedPassword = passwordEncoder.encode(decodedPassword);
                    old.setPassword(hashedPassword);
                } else {
                    return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Password length is to small. Min. 6 characters.").build();
                }
            }

            old.setLmod(new Date());
            getRepository().save(old);

            return Response.status(Response.Status.OK).entity("Updated successfully.").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Login has been not specified!").build();
        }
    }

    // Delete
    public String delete(User user) {
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

            String decodedPassword = new String(Base64.decode(password.getBytes()), StandardCharsets.UTF_8);

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

    public Response account(String login) {
        if (StringUtils.isNotEmpty(login)) {
            User loginResult = getRepository().findByLogin(login);

            if (loginResult == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("User with given login does't exist.").build();
            }

            return Response.status(Response.Status.OK).entity("Logged in successfully!").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("Username has been not specified!").build();
    }

    public boolean isUserAllowed(String username, String password, Set<String> rolesSet) {
        boolean result = false;

        if (StringUtils.isNotEmpty(username) && StringUtils.isNotBlank(username)) {
            User user = getRepository().findByLogin(username);
            result = user != null && passwordEncoder.matches(password, user.getPassword());
            result = result && isRoleAllowed(user.getUserRole().getName(), rolesSet);
        }

        return result;
    }

    private boolean isUserValid(String username, String password) {
        boolean result = false;

        if (StringUtils.isNotEmpty(username) && StringUtils.isNotBlank(username) && StringUtils.isNotEmpty(password) && StringUtils.isNotBlank(password)) {
            User user = getRepository().findByLogin(username);
            result = user != null && passwordEncoder.matches(password, user.getPassword());
        }

        return result;
    }

    private boolean isRoleAllowed(String userRole, Set<String> rolesSet) {
        boolean ret = false;

        if (StringUtils.isNotBlank(userRole) && StringUtils.isNotEmpty(userRole)) {
            for (String role : rolesSet) {
                if (userRole.equals(role)) {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    public UserRole getUserRoleByLogin(String login) {
        UserRole role = getRepository().findByLogin(login).getUserRole();
        role.getUsers().forEach(p -> p.setDeliveryAdresses(p.getDeliveryAdresses().stream().distinct().collect(Collectors.toList())));
        return role;
    }
}
