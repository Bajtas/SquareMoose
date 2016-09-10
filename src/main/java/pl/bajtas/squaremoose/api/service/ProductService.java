package pl.bajtas.squaremoose.api.service;

import org.hibernate.Hibernate;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMessage;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

@Service
public class ProductService implements ApplicationListener<ContextRefreshedEvent> {
  
  private static final Logger LOG = Logger.getLogger(ProductService.class);

    @Autowired  private ProductRepository productRepository;
    @Autowired  private EntityManager entityManager;
    
    public ProductRepository getRepository() {
      return productRepository;
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

        //Category shit = new Category();
        //shit.setName("Lol");



        //shirt.setCategory(shit);
        
        productRepository.save(shirt);
    }

    // Search by Product properties
    public Iterable<Product> getAll() {
        return getRepository().findAll();
    }

    public Page<Product> getAll(Integer page, Integer size) {
        if (page == null)
            return getRepository().findAll(new PageRequest(1, 20));
        if (size == null)
            return getRepository().findAll(new PageRequest(page, size));
        return null;
    }

    public List<Product> getAllWithCategory() {
        return getRepository().findByCategoryIsNotNull();
    }

    public List<Product> getAllWithoutCategory() {
        return getRepository().findByCategoryIsNull();
    }

    public Product getById(int id) {
        return getRepository().findOne(id);
    }

    public List<Product> getByNameContains(String name) {
        return getRepository().findByNameContainsIgnoreCase(name);
    }

    public List<Product> getByDescriptionContains(String description) {
        return getRepository().findByDescriptionContainsIgnoreCase(description);
    }

    public List<Product> getByPrice(Float price1, Float price2) throws Exception{
        if (price1 == null && price2 == null) {
            throw new Exception("Please specify price1 or price2 of Product for this request.");
        }
        else if (price1 != null && price2 != null) {
            return getRepository().findByPriceBetween(price1, price2);
        }
        else if (price1 == null) {
            return getRepository().findByPriceLessThan(price2);
        } else if (price2 == null) {
            return getRepository().findByPriceGreaterThan(price1);
        }

        return null;
    }

    public List<Product> searchProduct(String name, String description, Float price1, Float price2, String categoryMame) throws Exception {
        if (name == null && description == null && price1 == null && price2 == null && categoryMame == null)  {
            Iterable<Product> source = getAll();
            List<Product> allProducts = new ArrayList<>();
            source.forEach(allProducts::add);
            return allProducts;
        }
        List<List<Product>> results = new ArrayList<>();

        if (name != null) {
            results.add(getByNameContains(name));
        }

        if (description != null) {
            results.add(getByDescriptionContains(name));
        }

        if (price1 != null || price2 != null) {
            results.add(getByPrice(price1, price2));
        }

        if (categoryMame != null) {
            results.add(getByCategoryIdOrName(null, name));
        }

        return SearchUtil.combine(results);
    }

    /* --------------------------------------------------------------------------------------------- */

    // Search by Category properties
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
