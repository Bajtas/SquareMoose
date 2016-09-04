package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.ActualOrderState;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.repository.ActualOrderStateRepository;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;

import javax.persistence.EntityManager;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Service
public class ActualOrderStateService {
    private static final Logger LOG = Logger.getLogger(ActualOrderStateService.class);

    @Autowired
    private ActualOrderStateRepository actualOrderStateRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private EntityManager entityManager;

    public ActualOrderStateRepository getRepository() {
        return actualOrderStateRepository;
    }


    public Iterable<ActualOrderState> getAll() {
        return getRepository().findAll();
    }
}
