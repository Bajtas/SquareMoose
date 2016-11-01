package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Bajtas on 18.09.2016.
 */
@Entity
@JsonIdentityInfo(generator=JSOGGenerator.class)
@Table(name = "btorderstate")
public class OrderState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "orderState", fetch = FetchType.EAGER)
    private List<ActualOrderState> actualOrderStates;

    public OrderState(String name, String description, List<ActualOrderState> actualOrderStates) {
        this.name = name;
        this.description = description;
        this.actualOrderStates = actualOrderStates;
    }

    public OrderState(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public OrderState() {

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

    public List<ActualOrderState> getActualOrderStates() {
        return actualOrderStates;
    }

    public void setActualOrderStates(List<ActualOrderState> states) {
        this.actualOrderStates = states;
    }
}
