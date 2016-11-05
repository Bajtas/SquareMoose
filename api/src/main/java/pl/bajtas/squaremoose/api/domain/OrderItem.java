package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Bajtas on 03.09.2016.
 */

@Entity
@JsonIdentityInfo(generator = JSOGGenerator.class)
@Table(name = "btorderitem")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Integer amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
