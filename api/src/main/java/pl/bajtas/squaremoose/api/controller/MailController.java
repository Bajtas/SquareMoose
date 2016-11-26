package pl.bajtas.squaremoose.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bajtas.squaremoose.api.domain.Email;
import pl.bajtas.squaremoose.api.service.MailService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Bajtas on 26.11.2016.
 */
@Controller
@Path("/MailService")
public class MailController {
    @Autowired private MailService mailService;

    private MailService getService() {
        return mailService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/SendEmail")
    @PermitAll
    public Response sendEmail(Email email) {
        return getService().sendEmail(email);
    }
}
