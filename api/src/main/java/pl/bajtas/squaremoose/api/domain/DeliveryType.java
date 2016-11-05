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
@Table(name = "btdeliverytype")
public class DeliveryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "deliveryType", fetch = FetchType.EAGER)
    private List<Order> orders;

    private String name;

    private float price;

    private String time;

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof DeliveryType)) {
            return false;
        }

        DeliveryType otherDeliveryType = (DeliveryType) other;
        return id.equals(otherDeliveryType.getId());
    }
}
