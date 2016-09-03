package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.OrderItem;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.service.OrderItemService;
import pl.bajtas.squaremoose.api.service.ProductService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/OrderItemService")
public class OrderItemController {

    @Autowired
    OrderItemService orderItemService; // Product service bean for connection between controller and service layers

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orderitems")
    public Iterable<OrderItem> getAll() {
        return orderItemService.getAll();
    }
}
