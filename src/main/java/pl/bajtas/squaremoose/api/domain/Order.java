package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
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

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "order")
    private ActualOrderState actualOrderState;

    private float fullPrice;

    private int itemsAmount;

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

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Order)) {
            return false;
        }

        Order otherOrder = (Order) other;
        return id.equals(otherOrder.getId());
    }
}
