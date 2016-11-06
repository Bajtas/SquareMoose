package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Entity
@JsonIdentityInfo(generator = JSOGGenerator.class)
@Table(name = "btactualorderstate")
public class ActualOrderState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Date lmod;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="order_id")
    @Fetch(value = FetchMode.SELECT) // There is exception without this
    private Order order;
    @OneToMany(mappedBy = "actualOrderState", fetch = FetchType.EAGER)
    private List<OrderStateHistory> orderStateHistories;

    public ActualOrderState(String name, String description, Date lmod, Order order, List<OrderStateHistory> orderStateHistories) {
        this.name = name;
        this.description = description;
        this.lmod = lmod;
        this.order = order;
        this.orderStateHistories = orderStateHistories;
    }

    public ActualOrderState(String name, String description, Date lmod, Order order) {
        this.name = name;
        this.description = description;
        this.lmod = lmod;
        this.order = order;
    }

    public ActualOrderState(String name, String description, Order order) {
        this.name = name;
        this.description = description;
        this.lmod = new Date();
        this.order = order;
    }

    public ActualOrderState(String name, String description, Date lmod) {
        this.name = name;
        this.description = description;
        this.lmod = lmod;
    }

    public ActualOrderState(String name, String description) {
        this.name = name;
        this.description = description;
        this.lmod = new Date();
    }

    public ActualOrderState() {

    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderStateHistory> getOrderStateHistories() {
        return orderStateHistories;
    }

    public void setOrderStateHistories(List<OrderStateHistory> orderStateHistories) {
        this.orderStateHistories = orderStateHistories;
    }
}
