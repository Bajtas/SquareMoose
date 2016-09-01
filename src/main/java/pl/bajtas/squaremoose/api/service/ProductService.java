package pl.bajtas.squaremoose.api.service;

import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Date;

import javax.persistence.EntityManager;

@Service
public class ProductService implements ApplicationListener<ContextRefreshedEvent> {
  
  private static final Logger LOG = Logger.getLogger(ProductService.class);

    @Autowired  private ProductRepository productRepository;
    @Autowired  private CategoryRepository categoryRepository;
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
        
        Category shit = new Category();
        shit.setName("Lol");
        
        shirt.setCategory(shit);
        
        categoryRepository.save(shit);
        
        productRepository.save(shirt);
        
        
      
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
