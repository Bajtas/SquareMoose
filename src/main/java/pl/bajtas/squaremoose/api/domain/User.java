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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "btuser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;
    private String email;
    private String password;
    private Date addedOn;
    private boolean isOnline;
    private Date lmod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role")
    private UserRole userRole;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "btdeliveryadressrow", joinColumns = {
            @JoinColumn(name = "user_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "delivery_adress_id", nullable = false)})
    private List<DeliveryAdress> deliveryAdresses;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public List<DeliveryAdress> getDeliveryAdresses() {
        return deliveryAdresses;
    }

    public void setDeliveryAdresses(List<DeliveryAdress> deliveryAdresses) {
        this.deliveryAdresses = deliveryAdresses;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof User)) {
            return false;
        }

        User otherUser = (User) other;
        return id.equals(otherUser.getId());
    }
}
