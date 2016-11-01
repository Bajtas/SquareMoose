package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.service.ProductService;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
@Path("/ProductService")
public class ProductController {

    @Autowired
    ProductService productService; // Product service bean for connection between controller and service layers

    // Search by Product properties

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<Product> getAll() {
        return productService.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/page/{number}")
    @PermitAll
    public Page<Product> getPage(@PathParam("number") Integer page,
                                 @QueryParam("size") Integer size,
                                 @QueryParam("sortBy") String sortBy,
                                 @QueryParam("dir") String direction) {
        return productService.getAll(page, size, sortBy, direction);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/{id}")
    @PermitAll
    public Product getById(@NotNull @PathParam("id") Integer id) {
        return productService.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/name/{name}")
    @PermitAll
    public List<Product> getByNameContainsIgnoreCase(@NotNull @PathParam("name") String name) {
        return productService.getByNameContainsIgnoreCase(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/description/{description}")
    @PermitAll
    public List<Product> getByDescriptionContainsIgnoreCase(@NotNull @PathParam("description") String description) {
        return productService.getByDescriptionContainsIgnoreCase(description);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/price")
    @PermitAll
    public List<Product> getByNamePriceBetweenOrGreaterOrLess(@QueryParam("price1") Double price1, @QueryParam("price2") Double price2) throws Exception {
        return productService.getByPrice(price1, price2);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/search/page/{page}")
    @PermitAll
    public Page<Product> getByAllProperties (
            @NotNull @PathParam("page") int page,
            @QueryParam("pageSize") int pageSize,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("sortDir") String sortDir,
            @QueryParam("name") String name,
            @QueryParam("description") String description,
            @QueryParam("price1") Double price1,
            @QueryParam("price2") Double price2,
            @QueryParam("categoryName") String categoryMame) throws Exception {
        return productService.searchProduct(page, pageSize, sortBy, sortDir, name, description, price1, price2, categoryMame);
    }

    /* --------------------------------------------------------------------------------------------- */

    /* Find Product by Category properties */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/category")
    @PermitAll
    public List<Product> getByCategoryIdOrName(
            @QueryParam("id") Integer id,
            @QueryParam("name") String name) throws Exception {
        return productService.getByCategoryIdOrName(id, name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/categorized")
    @PermitAll
    public Iterable<Product> getAllWithCategoryNotNull() {
        return productService.getAllWithCategory();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products/uncategorized")
    @PermitAll
    public Iterable<Product> getAllWithCategoryNull() {
        return productService.getAllWithoutCategory();
    }
    /* --------------------------------------------------------------------------------------------- */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/product/add")
    @PermitAll
    public Response add(Product product) {
        return productService.add(product);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/product/{id}/update")
    @PermitAll
    public Response update(@NotNull @PathParam("id") int id, Product product) {
        return productService.update(id, product);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/product/{id}/delete")
    @PermitAll
    public String add(@NotNull @PathParam("id") int id) {
        return productService.delete(id);
    }
}
