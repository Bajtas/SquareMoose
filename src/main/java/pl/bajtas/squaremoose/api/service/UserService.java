package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.User;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.repository.UserRepository;

import javax.persistence.EntityManager;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class UserService {
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private EntityManager entityManager;

    public UserRepository getRepository() {
        return userRepository;
    }
//
//
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }
}
