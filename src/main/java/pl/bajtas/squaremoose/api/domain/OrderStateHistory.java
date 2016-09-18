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
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope = OrderStateHistory.class)
@Table(name = "btorderstatehistory")
public class OrderStateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Date lmod;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "orderStateHistories")
    private List<ActualOrderState> actualOrderStates;

    public OrderStateHistory(String name, String description, Date lmod, List<ActualOrderState> actualOrderStates) {
        this.name = name;
        this.description = description;
        this.lmod = lmod;
        this.actualOrderStates = actualOrderStates;
    }

    public OrderStateHistory(String name, String description, Date lmod) {
        this.name = name;
        this.description = description;
        this.lmod = lmod;
    }

    public OrderStateHistory(String name, String description) {
        this.name = name;
        this.description = description;
        this.lmod = new Date();
    }

    public OrderStateHistory() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActualOrderStates(List<ActualOrderState> actualOrderStates) {
        this.actualOrderStates = actualOrderStates;
    }

    public List<ActualOrderState> getActualOrderStates() {
        return actualOrderStates;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public Date getLmod() {
        return lmod;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof OrderStateHistory)) {
            return false;
        }

        OrderStateHistory otherOrderStateHistory = (OrderStateHistory) other;
        return id.equals(otherOrderStateHistory.getId());
    }
}
