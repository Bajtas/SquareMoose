package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.OrderItem;
import pl.bajtas.squaremoose.api.domain.PaymentMethod;
import pl.bajtas.squaremoose.api.service.OrderItemService;
import pl.bajtas.squaremoose.api.service.PaymentMethodService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Controller
@Path("/PaymentMethodService")
public class PaymentMethodController {

    @Autowired
    PaymentMethodService paymentMethodService; // Product service bean for connection between controller and service layers

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/methods")
    public Iterable<PaymentMethod> getAll() {
        return paymentMethodService.getAll();
    }
}
