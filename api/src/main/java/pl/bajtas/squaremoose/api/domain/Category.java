package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = JSOGGenerator.class)
@Table(name = "btcategory")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private Set<Product> products;

    public Category(int id, String name, Set<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public Category(String name, Set<Product> products) {
        this.name = name;
        this.products = products;
    }

    public Category(String name) {
        this.name = name;
    }

    public Category() {

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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Category)) {
            return false;
        }

        Category otherCategory = (Category) other;
        return id.equals(otherCategory.getId());
    }
}
