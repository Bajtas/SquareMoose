package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = DeliveryAdress.class)
@Table(name = "btdeliveryadress")
public class DeliveryAdress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private String address;
    private String town;
    private String zipCode;
    private String contactPhone;
    @Column(columnDefinition = "boolean default false")
    private boolean currentlyAssigned;
    @OneToMany(mappedBy = "deliveryAdress", fetch = FetchType.EAGER)
    private List<Order> orders;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "deliveryAdresses")
    private List<User> users;

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

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTown() {
        return town;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setCurrentlyAssigned(boolean currentlyAssigned) {
        this.currentlyAssigned = currentlyAssigned;
    }

    public boolean isCurrentlyAssigned() {
        return currentlyAssigned;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof DeliveryAdress)) {
            return false;
        }

        DeliveryAdress otherDeliveryAdress = (DeliveryAdress) other;
        return id.equals(otherDeliveryAdress.getId());
    }

}
