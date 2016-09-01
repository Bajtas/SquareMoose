package pl.bajtas.squaremoose.api.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "btcategory")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @OneToMany(mappedBy = "category")
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
