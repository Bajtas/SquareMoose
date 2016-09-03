package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "btcategory")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
  @JsonBackReference
  private Set<Product> products;

  public String getName() {
    return name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setProducts(Set<Product> products) {
    this.products = products;
  }

  public Set<Product> getProducts() {
    return products;
  }

}
