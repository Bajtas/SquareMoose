package pl.bajtas.squaremoose.api.dao;

import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.repository.ProductRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

@Repository
public class ProductDAO /*implements ApplicationListener<ContextRefreshedEvent>*/ {

    @Autowired  private EntityManager entityManager;

    private Logger log = Logger.getLogger(ProductDAO.class);

//    @Autowired
//    public void setProductRepository(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }
    
    
    
    

//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
      
//        Product shirt = new Product();
//        shirt.setDescription("Spring Framework Guru Shirt");
//        shirt.setPrice(new BigDecimal("18.95"));
//        shirt.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
//        shirt.setProductId("235268845711068308");
//        productRepository.save(shirt);
//
//        log.info("Saved Shirt - id: " + shirt.getId());
//
//        Product mug = new Product();
//        mug.setDescription("Spring Framework Guru Mug");
//        mug.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_coffee_mug-r11e7694903c348e1a667dfd2f1474d95_x7j54_8byvr_512.jpg");
//        mug.setProductId("168639393495335947");
//        mug.setPrice(new BigDecimal("8.92"));
//        productRepository.save(mug);
//        
//
//        log.info("Saved Mug - id:" + mug.getId());
//    }
    
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
