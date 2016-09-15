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
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope = UserRole.class)
@Table(name = "btuserrole")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Date lmod;

    @OneToMany(mappedBy = "userRole", fetch = FetchType.EAGER)
    private List<User> users;

    public UserRole() {

    }

    public UserRole(int id, String name, Date lmod) {
        this.id = id;
        this.name = name;
        this.lmod = lmod;
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

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
