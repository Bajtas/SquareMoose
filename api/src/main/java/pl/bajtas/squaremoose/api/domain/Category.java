package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIdentityInfo(generator=JSOGGenerator.class)
@Table(name = "btcategory")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Product> products;

    public Category(int id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public Category(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public Category(String name) {
        this.name = name;
    }

    public Category() {

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

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
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
