package pl.bajtas.squaremoose.api.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bajtas.squaremoose.api.domain.UserRole;

/**
 * Created by Bajtas on 04.09.2016.
 */
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
}
