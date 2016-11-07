package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.User;

import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 * <p>
 * Repository created for User Service.
 * Contains common methods to obtain data for User Service.
 * <p>
 * Some method got sql example above declaration.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    /* Find by User properties */
    // First method, by id is not needed
    Page<User> findAll(Pageable pageable);

    // Search by user login
    User findByLogin(String login);

    List<User> findByLoginContainsIgnoreCase(String name); // %login%

    List<User> findByLoginStartingWithIgnoreCase(String login); // login%

    // Search by user email
    User findByEmail(String email);

    List<User> findByEmailContainsIgnoreCase(String email); // %email%

    List<User> findByEmailStartingWithIgnoreCase(String login); // email%

    // Search by user role name
    List<User> findByUserRole_NameContainsIgnoreCase(String role); // %rolename%

    List<User> findByUserRole_NameStartingWithIgnoreCase(String role); // rolename%

    // Search if user is currently online
    List<User> findByIsOnline(Boolean online);


}
