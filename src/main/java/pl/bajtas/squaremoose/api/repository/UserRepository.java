package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.User;

import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface UserRepository extends CrudRepository<User,Integer> {

    /* Find by Product properties */
    // First method, by id is not needed
    //User findByName(String name);
    //List<User> findByNameContainsIgnoreCase(String name);
   // List<User> findByUserRole_Name(String name); // osmeckiego 37/21

    Page<User> findAll(Pageable pageable);

    List<User> findByLoginContainsIgnoreCase(String name);

    List<User> findByEmailContainsIgnoreCase(String email);

    List<User> findByUserRole_NameContainsIgnoreCase(String role);

    List<User> findByIsOnline(Boolean online);

    List<User> findByLoginStartingWithIgnoreCase(String login);
}
