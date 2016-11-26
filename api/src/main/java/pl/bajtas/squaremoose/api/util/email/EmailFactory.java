package pl.bajtas.squaremoose.api.util.email;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import pl.bajtas.squaremoose.api.domain.Email;
import pl.bajtas.squaremoose.api.domain.Order;
import pl.bajtas.squaremoose.api.domain.OrderItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Bajtas on 26.11.2016.
 */
public class EmailFactory {
    private final static Logger LOG = Logger.getLogger(EmailFactory.class);

    public enum EMAIL_TEMPLATE {
        ORDER_CREATED
    }

    private final EMAIL_TEMPLATE EMAIL_TYPE;

    private Email email = new Email();
    private String receiver = StringUtils.EMPTY;
    private StringBuilder text = new StringBuilder();

    public EmailFactory(EMAIL_TEMPLATE type) {
        EMAIL_TYPE = type;
    }

    public EmailFactory mailTo(String receiver) {
        this.receiver = receiver;

        return this;
    }

    public Email build(Object object) {
        switch (EMAIL_TYPE) {
            case ORDER_CREATED:
                if (object instanceof Order) {
                    Order order = (Order) object;
                    email.setSender("SquareMoose");
                    email.setSubject("Order has been created! Thank you for purchasing!");
                    email.setReceiver(receiver);
                    createOrderDetailsText(order);
                    email.setText(text.toString());
                } else {
                    LOG.error("Message not send! Object is not instance of order!");
                }
                break;
        }

        return email;
    }

    private void createOrderDetailsText(Order order) {
        String template = StringUtils.EMPTY;
        try {
            template = new Scanner(new File(EmailFactory.class.getResource("/other/email_template.html").getFile())).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        template = template.replace("{{ORDER_ID}}", order.getId().toString());
        template = template.replace("{{DELIVERY_NAME}}", order.getDeliveryAdress().getName());
        template = template.replace("{{DELIVERY_SURNAME}}", order.getDeliveryAdress().getSurname());
        template = template.replace("{{DELIVERY_ADDRESS}}", order.getDeliveryAdress().getAddress());
        template = template.replace("{{DELIVERY_ZIPCODE}}", order.getDeliveryAdress().getZipCode());
        template = template.replace("{{DELIVERY_TOWN}}", order.getDeliveryAdress().getTown());
        template = template.replace("{{DELIVERY_PHONE}}", order.getDeliveryAdress().getContactPhone());
        template = template.replace("{{DELIVERY_EMAIL}}", email.getReceiver());

        StringBuilder orderItemsString = new StringBuilder();
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemsString.append("<tr>");
                orderItemsString.append("<td align=\"center\">");
                orderItemsString.append(orderItem.getProduct().getName());
                orderItemsString.append("</td>");
                orderItemsString.append("<td align=\"center\">");
                orderItemsString.append(orderItem.getAmount());
                orderItemsString.append("</td>");
                orderItemsString.append("<td align=\"center\">");
                orderItemsString.append(orderItem.getAmount() * orderItem.getProduct().getPrice());
                orderItemsString.append("</td>");
            orderItemsString.append("</tr>");
        }

        template = template.replace("{{ORDER_ITEMS}}", orderItemsString);
        template = template.replace("{{DELIVERY_TYPE}}", order.getDeliveryType().getName());
        template = template.replace("{{DELIVERY_COST}}", String.valueOf(order.getDeliveryType().getPrice()));
        template = template.replace("{{DELIVERY_TIME}}", order.getDeliveryType().getTime());

        template = template.replace("{{TOTAL_COST}}", String.valueOf(order.getFullPrice()));
        template = template.replace("{{PAYMENT_METHOD}}", order.getPaymentMethod().getName());

        text.append(template);
    }
}
