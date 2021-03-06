package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.bajtas.squaremoose.api.domain.ProductImage;
import pl.bajtas.squaremoose.api.service.ProductImagesService;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by Bajtas on 03.09.2016.
 */
@Controller
@Path("/ProductImagesService")
@Produces(MediaType.APPLICATION_JSON)
public class ProductImagesResource {

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

    @PermitAll
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data",
            produces = {"application/json", "application/xml"})
    public String upload(@RequestParam("file") MultipartFile file) {
        String response = "ts";
        try {
            if (file.isEmpty()) {
                response = "Failed to store empty file " + file.getOriginalFilename();
            }
            Files.copy(file.getInputStream(), Paths.get(file.getOriginalFilename()), REPLACE_EXISTING);
        } catch (IOException e) {
            response = "Failed to store file " + file.getOriginalFilename();
        }

        return response;
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
