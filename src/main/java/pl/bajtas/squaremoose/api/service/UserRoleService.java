package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.domain.UserRole;
import pl.bajtas.squaremoose.api.repository.UserRepository;
import pl.bajtas.squaremoose.api.repository.UserRoleRepository;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class UserRoleService {
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Autowired
    private UserRoleRepository userRoleRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private EntityManager entityManager;

    public UserRoleRepository getRepository() {
        return userRoleRepository;
    }
    //
//
    public Iterable<UserRole> getAll() {
        return getRepository().findAll();
    }
}
