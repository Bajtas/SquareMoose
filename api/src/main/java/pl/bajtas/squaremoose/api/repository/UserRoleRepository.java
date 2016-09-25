package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.UserRole;

import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
    UserRole findByName(String name);
    List<UserRole> findByNameContainsIgnoreCase(String name);

    List<UserRole> findByUsers_LoginContainsIgnoreCase(String name);
}
