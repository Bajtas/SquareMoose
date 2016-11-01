package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 04.09.2016.
 */
@Entity
@JsonIdentityInfo(generator=JSOGGenerator.class)
@Table(name = "btuserrole")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Date lmod;
    @OneToMany(mappedBy = "userRole", fetch = FetchType.EAGER)
    private List<User> users;

    public UserRole(int id, String name, Date lmod) {
        this.id = id;
        this.name = name;
        this.lmod = lmod;
    }

    public UserRole(String name) {
        this.name = name;
        this.lmod = new Date();
    }

    public UserRole() {

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

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof UserRole)) {
            return false;
        }

        UserRole otherUserRole = (UserRole) other;
        return id.equals(otherUserRole.getId());
    }
}
