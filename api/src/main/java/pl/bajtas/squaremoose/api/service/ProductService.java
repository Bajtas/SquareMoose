package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductImagesRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.util.search.BListComparator;
import pl.bajtas.squaremoose.api.util.search.Combiner;
import pl.bajtas.squaremoose.api.util.search.PageUtil;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */

@Service
public class ProductService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = Logger.getLogger(ProductService.class);

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ProductImagesRepository productImagesRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ProductRepository getRepository() {
        return productRepository;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // Events
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Product shirt = new Product();
        shirt.setDescription("Spring Framework Guru Shirt");
        shirt.setAddedOn(new Date());
        shirt.setPrice(5);
        shirt.setLmod(new Date());
        shirt.setName("koszulka");

        productRepository.save(shirt);
    }

    // Search by Product properties
    @Transactional
    public Iterable<Product> getAll() {
        return getRepository().findAll();
    }

    @Transactional
    public Page<Product> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        PageUtil<Product> util = new PageUtil<>();
        return util.getPage(page, size, sortBy, sortDirection, getRepository());
    }

    @Transactional
    public Iterable<Product> getAllWithCategory() {
        return getRepository().findByCategoryIsNotNull();
    }

    @Transactional
    public Iterable<Product> getAllWithoutCategory() {
        return getRepository().findByCategoryIsNull();
    }

    @Transactional
    public Product getById(int id) {
        return getRepository().findOne(id);
    }

    @Transactional
    public List<Product> getByNameContainsIgnoreCase(String name) {
        return getRepository().findByNameContainsIgnoreCase(name);
    }

    @Transactional
    public List<Product> getByDescriptionContainsIgnoreCase(String description) {
        return getRepository().findByDescriptionContainsIgnoreCase(description);
    }

    @Transactional
    public List<Product> getByPrice(Double price1, Double price2) throws Exception {
        if (price1 == null && price2 == null) {
            throw new Exception("Please specify price1 or price2 of Product for this request.");
        } else if (price1 != null && price2 != null) {
            return getRepository().findByPriceBetween(price1, price2);
        } else if (price1 == null) {
            return getRepository().findByPriceLessThanEqual(price2);
        } else if (price2 == null) {
            return getRepository().findByPriceGreaterThanEqual(price1);
        } else if (price1 == 0 && price2 == 0) {
            return null;
        }

        return null;
    }

    @Transactional
    public Page<Product> searchProduct(Integer page, Integer pageSize, String sortBy, String sortDirection, String name,
                                       String description, Double price1, Double price2, String categoryMame) throws Exception {
        boolean unsorted = true;

        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }

        if (StringUtils.isNotEmpty(sortBy) || StringUtils.isNotBlank(sortBy)) {
            unsorted = false;
        }

        List<List<Product>> results = new ArrayList<>();

        if (StringUtils.isNotEmpty(name) && StringUtils.isNotBlank(name)) {
            results.add(getByNameContainsIgnoreCase(name));
        }

        if (StringUtils.isNotEmpty(description) && StringUtils.isNotBlank(description)) {
            results.add(getByDescriptionContainsIgnoreCase(description));
        }

        if (price1 != null || price2 != null) {
            results.add(getByPrice(price1, price2));
        }

        if (StringUtils.isNotEmpty(categoryMame) && StringUtils.isNotBlank(categoryMame)) {
            results.add(getByCategoryIdOrName(null, categoryMame));
        }

        Combiner<Product> combiner = new Combiner<>(results);
        List<Product> result = combiner.combine();
        if (!unsorted) {
            if (result == null) {
                return getRepository().findAll(new PageRequest(page, pageSize, SearchUtil.determineSortDirection(sortDirection), sortBy));
            }

            result.sort(new BListComparator(sortBy, sortDirection));
        }

        if (result == null)
            return null;

        int max = (pageSize * (page + 1) > result.size()) ? result.size() : pageSize * (page + 1);
        if (max < pageSize * page)
            return null;
        Page<Product> pageResult = new PageImpl<>(result.subList(page * pageSize, max), null, result.size());
        return pageResult;
    }

    /* --------------------------------------------------------------------------------------------- */

    // Search by Category properties
    @Transactional
    public List<Product> getByCategoryIdOrName(Integer id, String name) throws Exception {
        if (id == null && name == null) {
            throw new Exception("Please specify Id or Name of Category for this request.");
        } else if (id == null) {
            name = name.toLowerCase();
            return getRepository().findByCategory_NameContainsIgnoreCase(name);
        } else if (name == null) {
            return getRepository().findByCategory_Id(id);
        }

        return null;
    }

    /* --------------------------------------------------------------------------------------------- */

    // Add new product to DB
    @Transactional
    public Response add(Product product) {
        Category category = product.getCategory();
        List<ProductImage> productImages = product.getImages();

        try {
            if (category != null) {
                categoryRepository.save(category);
            }
            if (productImages != null) {
                for (ProductImage image : productImages) {
                    productImagesRepository.save(image);
                }
            }

            productRepository.save(product);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("Product added successfully!").build();
    }

    // Update existing
    @Transactional
    public Response update(int id, Product product) {
        LOG.info("Trying to save product with id: " + id);

        Category category = product.getCategory();
        List<ProductImage> productImages = product.getImages();

        try {
            Product old = getRepository().findOne(id);
            old.setDescription(product.getDescription());
            old.setName(product.getName());
            old.setPrice(product.getPrice());

            if (old != null) {
                if (category != null && category.getId() == null) {
                    categoryRepository.save(category);
                }
                old.setCategory(category);
                if (productImages != null) {
                    for (ProductImage image : productImages) {
                        if (image.getId() == null) {
                            image.setAddedOn(new Date());
                            productImagesRepository.save(image);
                        }
                    }
                    old.setImages(productImages);
                }

                old.setLmod(new Date());

                productRepository.save(old);
                return Response.status(Response.Status.OK).entity("Category with id: " + id + " updated successfully!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Category with given id: " + id + " not found!").build();
            }
        } catch (Exception e) {
            LOG.error("Error occured when saved: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }
    }

    // Delete
    @Transactional
    public String delete(int id) {
        LOG.info("Trying to delete Product.");
        String info = "Deleted successfully!";

        LOG.info("Product with id: " + id + " will be deleted.");

        Product product = getRepository().findOne(id);
        if (product != null) {
            try {
                List<ProductImage> images = product.getImages();
                for (ProductImage image : images) {
                    productImagesRepository.delete(image);
                }

                product.setCategory(null);
                getRepository().save(product);
                getRepository().delete(id);
            } catch (Exception e) {
                LOG.error("Product with id: " + id, e);
                info = "Error occured!";
            }
        } else {
            info = "Product with given id: " + id + " not found!";
        }
        return info;
    }

    /* --------------------------------------------------------------------------------------------- */

    public List<Product> getByOrderId(Integer id) {
        return getRepository().findByOrderItems_Order_Id(id);
    }

//    @Bean
//    public SessionFactory<FTPFile> ftpSessionFactory() {
//        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
//        sf.setHost("localhost");
//        sf.setPort(port);
//        sf.setUsername("foo");
//        sf.setPassword("foo");
//        return new CachingSessionFactory<FTPFile>(sf);
//    }
}
