package pl.bajtas.squaremoose.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.service.ProductService;

@Component
@Path("/health")
public class IndexController {

  @Autowired ProductService productService;
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/lol")
  public Iterable<Product> index() {
    return productService.getRepository().findAll();
  }

}
