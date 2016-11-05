package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Entity
@JsonIdentityInfo(generator = JSOGGenerator.class)
@Table(name = "btpaymentmethod")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "paymentMethod", fetch = FetchType.EAGER)
    private List<Order> orders;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof PaymentMethod)) {
            return false;
        }

        PaymentMethod otherPaymentMethod = (PaymentMethod) other;
        return id.equals(otherPaymentMethod.getId());
    }
}
