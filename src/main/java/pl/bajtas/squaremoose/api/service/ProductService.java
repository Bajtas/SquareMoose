package pl.bajtas.squaremoose.api.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.DeliveryAdress;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductImagesRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.util.search.Combiner;
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

    @Autowired  private SessionFactory sessionFactory;
    @Autowired  private ProductImagesRepository productImagesRepository;
    @Autowired  private ProductRepository productRepository;
    @Autowired  private CategoryRepository categoryRepository;
    
    public ProductRepository getRepository() {
      return productRepository;
    }

    protected Session getSession() {
        return  sessionFactory.getCurrentSession();
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
        boolean unsorted = false;
        Sort.Direction direction;

        if (page == null)
            page = 0;
        if (size == null)
            size = 20;
        if (StringUtils.isEmpty(sortBy))
            unsorted = true;

        Page<Product> result;
        if (!unsorted) {
            direction = SearchUtil.determineSortDirection(sortDirection);

            result = getRepository().findAll(new PageRequest(page, size, direction, sortBy));
        }
        else
            result = getRepository().findAll(new PageRequest(page, size));

        return result;
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
    public List<Product> getByPrice(Double price1, Double price2) throws Exception{
        if (price1 == null && price2 == null) {
            throw new Exception("Please specify price1 or price2 of Product for this request.");
        }
        else if (price1 != null && price2 != null) {
            return getRepository().findByPriceBetween(price1, price2);
        }
        else if (price1 == null) {
            return getRepository().findByPriceLessThanEqual(price2);
        } else if (price2 == null) {
            return getRepository().findByPriceGreaterThanEqual(price1);
        }

        return null;
    }

    @Transactional
    public List<Product> searchProduct(String name, String description, Double price1, Double price2, String categoryMame) throws Exception {
        if (name == null && description == null && price1 == null && price2 == null && categoryMame == null)  {
            Iterable<Product> source = getAll();
            List<Product> allProducts = new ArrayList<>();
            source.forEach(allProducts::add);
            return allProducts;
        }
        List<List<Product>> results = new ArrayList<>();

        if (name != null) {
            results.add(getByNameContainsIgnoreCase(name));
        }

        if (description != null) {
            results.add(getByDescriptionContainsIgnoreCase(name));
        }

        if (price1 != null || price2 != null) {
            results.add(getByPrice(price1, price2));
        }

        if (categoryMame != null) {
            results.add(getByCategoryIdOrName(null, name));
        }

        Combiner<Product> combiner = new Combiner<>(results);
        return combiner.combine();
    }

    /* --------------------------------------------------------------------------------------------- */

    // Search by Category properties
    @Transactional
    public List<Product> getByCategoryIdOrName(Integer id, String name) throws Exception {
        if (id == null && name == null) {
            throw new Exception("Please specify Id or Name of Category for this request.");
        }
        else if (id == null) {
            name = name.toLowerCase();
            return getRepository().findByCategory_NameContainsIgnoreCase(name);
        }
        else if (name == null) {
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
            if (old != null) {
                product.setId(id);
                if (category != null) {
                    categoryRepository.save(category);
                }
                if (productImages != null) {
                    for (ProductImage image : productImages) {
                        productImagesRepository.save(image);
                    }
                }

                product.setLmod(new Date());

                productRepository.save(product);
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
