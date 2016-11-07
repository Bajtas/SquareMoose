package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.service.generic.GenericService;
import pl.bajtas.squaremoose.api.util.search.CategoryStats;
import pl.bajtas.squaremoose.api.util.search.PageUtil;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */
@Service
public class CategoryService implements GenericService<Category, CategoryRepository>, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    public CategoryRepository getRepository() {
        return categoryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    /* Returns all Categories */
    @Override
    public Iterable<Category> getAll() {
        LOG.info("Returns all categories.");

        return getRepository().findAll();
    }

    @Override
    public Page<Category> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        PageUtil<Category> util = new PageUtil<>();
        return util.getPage(page, size, sortBy, sortDirection, getRepository());
    }

    /* Returns one Category by Id */
    @Override
    public Category getById(int id) {
        LOG.info("Returns Category related to id: " + id);

        return getRepository().findOne(id);
    }

    /* Returns one Category by Name - Ignores case */
    public Iterable<Category> getByNameContainsIgnoreCase(String name) {
        LOG.info("Returns Category related to Name: " + name + " | Ignores case");

        return getRepository().findByNameContainsIgnoreCase(name);
    }

    /* Returns stats for all Categories */
    public List<CategoryStats> getCategoryStats(boolean byId, boolean byName) throws Exception {
        LOG.info("Getting stats for all Categories.");

        Iterable<Category> allCategories = categoryRepository.findAll(); // Get all categories from DB
        List<CategoryStats> results = new ArrayList<>(); // New empty array for results
        for (Category category : allCategories) {
            CategoryStats stats = new CategoryStats(); // Create new CategoryStats obj
            stats.setName(category.getName()); // populate it

            // ProductRepository used to get count of Category
            if (byId) {
                stats.setCount(productRepository.countByCategory_Id(category.getId()));
            } else if (byName) {
                stats.setCount(productRepository.countDistinctByCategory_Name(category.getName()));
            } else {
                return null;
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
    @Override
    public Response add(Category category) {
        LOG.info("Trying to save category: " + category.getName() + category.getId());

        if (isCategoryExists(category.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: Category with id: " + category.getId() + " already exist.").build();
        }

        try {
            getRepository().save(category);
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occured when saved: " + e.toString()).build();
        }

        LOG.info("Object added successfully!");
        return Response.status(Response.Status.OK).entity("Category added successfully!").build();
    }

    @Override
    public Response update(int id, Category updatedCategory) {
        LOG.info("Trying to save category with id: " + id);

        try {
            Category old = getRepository().findOne(id);
            if (old != null) {
                updatedCategory.setId(id);

                getRepository().save(updatedCategory);
                return Response.status(Response.Status.OK).entity("Category with id: " + id + " updated successfully!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Category with given id: " + id + " not found!").build();
            }
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }
    }

    /* Deletes one Category assigned to given id */
    @Override
    public Response delete(int id) {
        LOG.info("Trying to delete Category.");
        String info = "Deleted successfully!";

        LOG.info("Category with id: " + id + " will be deleted.");

        Category category = getRepository().findOne(id);
        if (category != null) {
            try {
                List<Product> assignedProducts = category.getProducts();
                for (Product product : assignedProducts) {
                    product.setCategory(null);
                    productRepository.save(product);
                }
                getRepository().delete(id);

                return Response.status(Response.Status.OK).entity("Category with id: " + id + " deleted successfully!").build();
            } catch (Exception e) {
                LOG.error("Category with id: " + id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Category with given id: " + id + " not found!").build();
        }
    }

    /* Util method, to check if Category with given Id exist. */
    private boolean isCategoryExists(Integer id) {
        return id != null && getRepository().findOne(id) != null;
    }
}
