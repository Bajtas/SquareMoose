package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "btactualorderstate")
public class ActualOrderState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date lmod;

    private String name;

    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "order")
    //@JsonManagedReference
    @PrimaryKeyJoinColumn
    private Order order;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
