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
    //@JsonManagedReference
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_type")
   // @JsonManagedReference
    private DeliveryType deliveryType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method")
   // @JsonManagedReference
    private PaymentMethod paymentMethod;


    @OneToOne(fetch = FetchType.EAGER, mappedBy = "order")
    private ActualOrderState actualOrderState;

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

//    public ActualOrderState getActualOrderState() {
//        return actualOrderState;
//    }
//
//    public void setActualOrderState(ActualOrderState actualOrderState) {
//        this.actualOrderState = actualOrderState;
//    }

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
}
