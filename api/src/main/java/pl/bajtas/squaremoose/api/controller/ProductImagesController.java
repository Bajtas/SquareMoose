package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.service.ProductImagesService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */
@Controller
@Path("/ProductImagesService")
public class ProductImagesController {

    @Autowired
    ProductImagesService productImagesService; // ProductImages service bean for connection between controller and service layers

    // Read

    //region Description
    /* Main method
    * Takes 0 parameters
    *
    * Returns list of all ProductImages
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/images")
    public Iterable<ProductImage> getAll() {
        return productImagesService.getAll();
    }

    //region Description
    /* Search by Id
    * Takes 1 parameters - Id
    *
    * Returns one ProductImage related to this Id
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/images/id/{id}")
    public ProductImage getById(@NotNull @PathParam("id") int id) {
        return productImagesService.getById(id);
    }

    //region Description
    /* Search by ImageSrc
    * Takes 1 parameters - ImageSrc
    *
    * Returns one ProductImage related to this ImageSrc
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/images/src/{src}")
    public ProductImage getByImageSrc(@NotNull @PathParam("src") String src) {
        return productImagesService.getByImageSrc(src);
    }

    //region Description
    /* Search by productId or productName
    * Takes 2 parameters - productId and/or productName
    *
    * Returns list of ProductImage related to this query
    *
    * !Important, if none of parameters is defined, this will return full ProductImages list.
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/images/search")
    public List<ProductImage> getByAllProperties(
            @QueryParam("productId") Integer productId,
           @QueryParam("productName") String productName) {
        return productImagesService.searchProductImage(productId, productName);
    }

    // Add
    //region Description
    /* Add new ProductImage to DB
    * Takes 1 parameter - ProductImage in JSON format
    *
    * Returns info
    *
    * If not succeed, returns info about error.
    * */
    //endregion
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/images/add")
    public String add(ProductImage productImage) {
        return productImagesService.add(productImage); // true for add new
    }

    //region Description
    /* Update category in DB
    * Takes 1 parameter - Category in JSON format
    *
    * Returns info
    *
    * If not succeed, returns info about error.
    * */
    //endregion
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/image/{id}/update")
    public String update(@NotNull @PathParam("id") int id, ProductImage productImage) {
        return productImagesService.update(id, productImage);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/images/stats")
    public String getStats() {
        return productImagesService.getStats();
    }


}
