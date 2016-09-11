package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductImagesRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;
import pl.bajtas.squaremoose.api.util.search.Combiner;
import pl.bajtas.squaremoose.api.util.search.SearchUtil;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */
@Service
public class ProductImagesService {

    private static final Logger LOG = Logger.getLogger(ProductImagesService.class);

    @Autowired  private ProductImagesRepository productImagesRepository;

    public ProductImagesRepository getRepository() {
        return productImagesRepository;
    }

    public Iterable<ProductImage> getAll() {
        return productImagesRepository.findAll();
    }

    public String getStats() {
        return "All images in system: " +  productImagesRepository.count();
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

    private List<ProductImage> getByProductsNameContains(String productName) {
        return getRepository().getByProducts_NameContainsIgnoreCase(productName);
    }

    private List<ProductImage> getByProductsId(Integer productId) {
        return getRepository().getByProducts_Id(productId);
    }


    public String addOrUpdate(ProductImage productImage, boolean update) {
        LOG.info("Trying to save product image: " + productImage.getImageSrc());

        // If true check if there is obj related to this Id in DB
        update = productImage != null ? isImageExists(productImage.getId()) : false;

        boolean error = false;
        String info = "Object ";

        // Error
        if (update && (productImage == null || productImage.getId() == null)) {
            info = "Error occured when saved:";
            info += productImage == null ? " ProductImage is null!" : "ProductImage.Id is null!";
            return info;
        }

        try {
            getRepository().save(productImage);
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

    private boolean isImageExists(Integer id) {
        return getRepository().findOne(id) != null ? true : false;
    }
}
