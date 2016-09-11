package pl.bajtas.squaremoose.api.controller;

import javax.print.attribute.standard.Media;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.service.ProductService;

import java.util.List;

@Controller
@Path("/ProductService")
public class ProductController {

    @Autowired
    ProductService productService; // Product service bean for connection between controller and service layers

    // Search by Product properties

    //region Description
    /* Main method
    * Takes 0 parameters
    *
    * Returns list of all products with and without categories
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products")
    public Iterable<Product> getAll() {
        return productService.getAll();
    }

    //region Description
    /* Main method
    * Takes 1 parameter - Number of page
    *
    * Returns list of all products on this page
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/page/{number}")
    public Page<Product> getPage(@PathParam("number") Integer page,
                                 @QueryParam("size") Integer size,
                                 @QueryParam("sortBy") String sortBy,
                                 @QueryParam("dir") String direction) {
        return productService.getAll(page, size, sortBy, direction);
    }

    //region Description
    /* getById()
    * Takes 1 parameter - product id
    *
    * Returns one product assigned to this id
    * */
    //endregion
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/{id}")
    public Product getById(@NotNull @PathParam("id") Integer id) {
        return productService.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/name/{name}")
    public List<Product> getByNameContains(@NotNull @PathParam("name") String name) {
        return productService.getByNameContains(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/description/{description}")
    public List<Product> getByDescriptionContains(@NotNull @PathParam("description") String description) {
        return productService.getByDescriptionContains(description);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/price")
    public List<Product> getByNamePriceBetweenOrGreaterOrLess(@QueryParam("price1") Float price1, @QueryParam("price2") Float price2) throws Exception {
        return productService.getByPrice(price1, price2);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/search")
    public List<Product> getByAllProperties (
            @QueryParam("name") String name,
            @QueryParam("description") String description,
            @QueryParam("price1") Float price1,
            @QueryParam("price2") Float price2,
            @QueryParam("categoryName") String categoryMame) throws Exception {
        return productService.searchProduct(name, description, price1, price2, categoryMame);
    }
    /* --------------------------------------------------------------------------------------------- */

    /* Find Product by Category properties */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/category")
    public List<Product> getByCategoryIdOrName(
            @QueryParam("id") Integer id,
            @QueryParam("name") String name) throws Exception {
        return productService.getByCategoryIdOrName(id, name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/categorized")
    public List<Product> getAllWithCategoryNotNull() {
        return productService.getAllWithCategory();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/uncategorized")
    public List<Product> getAllWithCategoryNull() {
        return productService.getAllWithoutCategory();
    }
    /* --------------------------------------------------------------------------------------------- */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/products/add")
    public String add(Product product) {
        return productService.add(product);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/product/{id}/update")
    public String add(@NotNull @PathParam("id") int id, Product product) {
        return productService.update(id, product);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/product/{id}/delete")
    public String add(@NotNull @PathParam("id") int id) {
        return productService.delete(id);
    }
}
