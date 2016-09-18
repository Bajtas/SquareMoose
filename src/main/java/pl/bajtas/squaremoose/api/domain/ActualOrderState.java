package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.org.apache.xpath.internal.operations.Or;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope = ActualOrderState.class)
@Table(name = "btactualorderstate")
public class ActualOrderState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Date lmod;
    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Order order;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_state_id")
    private OrderState orderState;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "btorderhistoryrow", joinColumns = {
            @JoinColumn(name = "actual_order_state_id", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "order_state_history_id", nullable = false) })
    private List<OrderStateHistory> orderStateHistories;

    public ActualOrderState(String name, String description, Date lmod, Order order, List<OrderStateHistory> orderStateHistories)
    {
        this.name = name;
        this.description = description;
        this.lmod = lmod;
        this.order = order;
        this.orderStateHistories = orderStateHistories;
    }

    public ActualOrderState(String name, String description, Date lmod, Order order)
    {
        this.name = name;
        this.description = description;
        this.lmod = lmod;
        this.order = order;
    }

    public ActualOrderState(String name, String description, Order order)
    {
        this.name = name;
        this.description = description;
        this.lmod = new Date();
        this.order = order;
    }

    public ActualOrderState(String name, String description, Date lmod)
    {
        this.name = name;
        this.description = description;
        this.lmod = lmod;
    }

    public ActualOrderState(String name, String description)
    {
        this.name = name;
        this.description = description;
        this.lmod = new Date();
    }

    public ActualOrderState() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderStateHistories(List<OrderStateHistory> orderStateHistories) {
        this.orderStateHistories = orderStateHistories;
    }

    public List<OrderStateHistory> getOrderStateHistories() {
        return orderStateHistories;
    }
}
