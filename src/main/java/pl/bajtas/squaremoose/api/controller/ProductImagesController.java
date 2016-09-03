package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.service.ProductImagesService;
import pl.bajtas.squaremoose.api.service.ProductService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Bajtas on 03.09.2016.
 */
@Controller
@Path("/ProductImagesService")
public class ProductImagesController {
    @Autowired
    ProductImagesService productImagesService; // Product service bean for connection between controller and service layers

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/images")
    public Iterable<ProductImage> getAll() {
        return productImagesService.getAll();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/images/stats")
    public String getStats() {
        return productImagesService.getStats();
    }
}
