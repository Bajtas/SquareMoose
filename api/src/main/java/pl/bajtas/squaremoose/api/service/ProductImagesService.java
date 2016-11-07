package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.repository.ProductImagesRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.util.search.Combiner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */
@Service
public class ProductImagesService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = Logger.getLogger(ProductImagesService.class);

    @Autowired
    private ProductImagesRepository productImagesRepository;
    @Autowired
    private ProductRepository productRepository;

    public ProductImagesRepository getRepository() {
        return productImagesRepository;
    }

    // Events
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    // Search by Product Image properties
    public Iterable<ProductImage> getAll() {
        return productImagesRepository.findAll();
    }

    public ProductImage getById(int id) {
        return getRepository().findOne(id);
    }

    public ProductImage getByImageSrc(String src) {
        return getRepository().findByImageSrcContainsIgnoreCase(src);
    }

    public List<ProductImage> searchProductImage(Integer productId, String productName) {
        if (productId == null && productName == null) {
            Iterable<ProductImage> source = getAll();
            List<ProductImage> allProductImages = new ArrayList<>();
            source.forEach(allProductImages::add);
            return allProductImages;
        }

        List<List<ProductImage>> results = new ArrayList<>();

        if (productId != null) {
            results.add(getByProductsId(productId));
        }

        if (productName != null) {
            results.add(getByProductsNameContains(productName));
        }

        Combiner<ProductImage> combiner = new Combiner<>(results);
        return combiner.combine();
    }

    public String getStats() {
        return "All images in system: " + productImagesRepository.count();
    }

    /* --------------------------------------------------------------------------------------------- */

    // Search by Product properties
    private List<ProductImage> getByProductsNameContains(String productName) {
        return getRepository().getByProducts_NameContainsIgnoreCase(productName);
    }

    private List<ProductImage> getByProductsId(Integer productId) {
        return getRepository().getByProducts_Id(productId);
    }

    /* --------------------------------------------------------------------------------------------- */

    // Add new image to DB
    public String add(ProductImage images) {
        try {
            getRepository().save(images);
        } catch (Exception e) {
            return "Error: " + e;
        }

        return "Product Image added successfully!";
    }

    // Update existing
    public String update(int id, ProductImage productImage) {
        productImage.setId(id);

        try {
            productImagesRepository.save(productImage);
        } catch (Exception e) {
            return "Error: " + e;
        }

        return "Product Image updated!";
    }

    // Delete
    public String delete(int id) {
        LOG.info("Trying to delete ProductImage.");
        String info = "Deleted successfully!";

        LOG.info("ProductImage with id: " + id + " will be deleted.");

        ProductImage image = getRepository().findOne(id);
        if (image != null) {
            try {
                List<Product> products = image.getProducts();
                for (Product product : products)
                    product.setImages(null);

                productRepository.save(products);
                getRepository().delete(id);
            } catch (Exception e) {
                LOG.error("ProductImage with id: " + id, e);
                info = "Error occured!";
            }
        } else {
            info = "ProductImage with given id: " + id + " not found!";
        }
        return info;
    }

    /* --------------------------------------------------------------------------------------------- */

    // Other
    private boolean isImageExists(Integer id) {
        return getRepository().findOne(id) != null;
    }
}
