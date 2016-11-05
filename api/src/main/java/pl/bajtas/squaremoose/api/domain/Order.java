package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Entity
@JsonIdentityInfo(generator = JSOGGenerator.class)
@Table(name = "btorder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_type")
    private DeliveryType deliveryType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method")
    private PaymentMethod paymentMethod;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_adress")
    private DeliveryAdress deliveryAdress;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne( mappedBy = "order") //fetch = FetchType.EAGER,
    private ActualOrderState actualOrderState;
    private float fullPrice;
    private int itemsAmount;
    private Date addedOn;
    private Date lmod;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public ActualOrderState getActualOrderState() {
        return actualOrderState;
    }

    public void setActualOrderState(ActualOrderState actualOrderState) {
        this.actualOrderState = actualOrderState;
    }

    public float getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(float fullPrice) {
        this.fullPrice = fullPrice;
    }

    public int getItemsAmount() {
        return itemsAmount;
    }

    public void setItemsAmount(int itemsAmount) {
        this.itemsAmount = itemsAmount;
    }

    public DeliveryAdress getDeliveryAdress() {
        return deliveryAdress;
    }

    public void setDeliveryAdress(DeliveryAdress deliveryAdress) {
        this.deliveryAdress = deliveryAdress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Order)) {
            return false;
        }

        Order otherOrder = (Order) other;
        return id.equals(otherOrder.getId());
    }
}
