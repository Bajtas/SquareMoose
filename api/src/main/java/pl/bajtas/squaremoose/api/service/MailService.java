package pl.bajtas.squaremoose.api.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.bajtas.squaremoose.api.domain.Email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.Response;

/**
 * Created by Bajtas on 26.11.2016.
 */
@Service
public class MailService {
    private static final Logger LOG = Logger.getLogger(MailService.class);

    @Autowired private JavaMailSender javaMailSender;

    public Response sendEmail(Email email) {
        LOG.info("Invoke - Send Email");
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(email.getReceiver());
            helper.setReplyTo(email.getSender());
            helper.setFrom(email.getSender());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText());

            javaMailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e).build();
        }

        return Response.status(Response.Status.OK).entity("Email sent successfully!").build();
    }
}
