package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.util.search.CategoryStats;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */
@Service
public class CategoryService {
    private static final Logger LOG = Logger.getLogger(ProductService.class);

    @Autowired  private CategoryRepository categoryRepository;
    @Autowired  private ProductRepository productRepository;
    @Autowired  private EntityManager entityManager;

    public CategoryRepository getRepository() {
        return categoryRepository;
    }

    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(int id) {
        return categoryRepository.findOne(id);
    }

    public List<Category> getByName(String name) {
        return categoryRepository.findByNameContainsIgnoreCase(name);
    }

    public List<CategoryStats> getCategoryStats(boolean byId, boolean byName) {
        Iterable<Category> allCategories = categoryRepository.findAll();
        List<CategoryStats> results = new ArrayList<CategoryStats>();
        for (Category category : allCategories) {
            CategoryStats stats = new CategoryStats();
            stats.setName(category.getName());

            if (byId) {
                stats.setCount(productRepository.countByCategory_Id(category.getId()));
            } else if (byName) {
                stats.setCount(productRepository.countDistinctByCategory_Name(category.getName()));
            }

            results.add(stats);
        }
        return results;
    }
}
