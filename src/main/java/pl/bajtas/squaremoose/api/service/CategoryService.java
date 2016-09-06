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

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntityManager entityManager;

    public CategoryRepository getRepository() {
        return categoryRepository;
    }

    /* Returns all Categories */
    public Iterable<Category> getAll() {
        LOG.info("Returns all categories.");

        return getRepository().findAll();
    }

    /* Returns one Category by Id */
    public Category getById(int id) {
        LOG.info("Returns Category related to id: " + id);

        return getRepository().findOne(id);
    }

    /* Returns one Category by Name - Ignores case */
    public List<Category> getByName(String name) {
        LOG.info("Returns Category related to Name: " + name + " | Ignores case");

        return getRepository().findByNameContainsIgnoreCase(name);
    }

    /* Returns stats for all Categories */
    public List<CategoryStats> getCategoryStats(boolean byId, boolean byName) throws Exception {
        LOG.info("Getting stats for all Categories.");

        Iterable<Category> allCategories = categoryRepository.findAll(); // Get all categories from DB
        List<CategoryStats> results = new ArrayList<CategoryStats>(); // New empty array for results
        for (Category category : allCategories) {
            CategoryStats stats = new CategoryStats(); // Create new CategoryStats obj
            stats.setName(category.getName()); // populate it

            // ProductRepository used to get count of Category
            if (byId) {
                stats.setCount(productRepository.countByCategory_Id(category.getId()));
            } else if (byName) {
                stats.setCount(productRepository.countDistinctByCategory_Name(category.getName()));
            }

            // Add this stat to results
            results.add(stats);
        }

        LOG.info("Stats results ready, checking it's size.");

        // if Results are empty
        if (results.size() == 0) {
            throw new Exception("There is no results! Or maybe you forgot to specify Id or Name for this query ?");
        }

        LOG.info("Returning Categories stats with size: " + results.size());
        return results;
    }

    /* Returns info about saving or updating Category */
    public String addOrUpdate(Category category, boolean update) {
        LOG.info("Trying to save category: " + category.getName() + category.getId());

        // If true check if there is obj related to this Id in DB
        update = category != null ? isCategoryExists(category.getId()) : false;

        boolean error = false;
        String info = "Object ";

        // Error
        if (update && (category == null || category.getId() == null)) {
            info = "Error occured when saved:";
            info += category == null ? " Category is null!" : "Category.Id is null!";
            return info;
        }

        try {
            getRepository().save(category);
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            info = "Error occured when saved: " + e.toString();
            error = true;
        }
        // Success
        if (!error) {
            LOG.info("Object saved successfully!");
            info += update ? "updated successfully!" : "saved successfully!";
        }

        return info;
    }

    public String deleteByIdOrName(Integer id, String name) {
        LOG.info("Trying to delete Category." );
        String info = "Deleted successfully!";

        if (id == null && name == null) {
            LOG.warn("Id and Name for Category to delete has not been specified!" );

            return "Please specify Id or Name of Category to delete it.";
        } else if (id != null) {
            LOG.info("Category with id: " + id + " will be deleted.");

            try {
                getRepository().delete(id);
            } catch (Exception e) {
                LOG.error("Category with id: " + id, e);
                info = "Error occured!";
            }
        } else if (name != null) {
            LOG.info("Category with name: " + name + " will be deleted." );

            try {
                getRepository().deleteByName(name);
            } catch (Exception e) {
                LOG.error("Category with name: " + name, e);
                info = "Error occured!";
            }
        }

        return info;
    }

    /* Util method, to check if Category with given Id exist. */
    private boolean isCategoryExists(Integer id) {
        return getRepository().findOne(id) != null ? true : false;
    }
}
