package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductImagesRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;

import javax.persistence.EntityManager;

/**
 * Created by Bajtas on 03.09.2016.
 */
@Service
public class ProductImagesService {

    private static final Logger LOG = Logger.getLogger(ProductImagesService.class);

    @Autowired  private CategoryRepository categoryRepository;
    @Autowired  private ProductRepository productRepository;
    @Autowired  private ProductImagesRepository productImagesRepository;
    @Autowired  private EntityManager entityManager;

    public Iterable<ProductImage> getAll() {
        return productImagesRepository.findAll();
    }

    public String getStats() {
        return "All images in system: " +  productImagesRepository.count();
    }
}
