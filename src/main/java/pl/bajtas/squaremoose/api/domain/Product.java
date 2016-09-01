package pl.bajtas.squaremoose.api.domain;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "btproduct")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "category")
  private Category category;

  private String name;

  private float price;

  private String description;

  private Date addedOn;

  private Date lmod;

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setAddedOn(Date addedOn) {
    this.addedOn = addedOn;
  }

  public Date getAddedOn() {
    return addedOn;
  }

  public void setLmod(Date lmod) {
    this.lmod = lmod;
  }

  public Date getLmod() {
    return lmod;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public float getPrice() {
    return price;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Category getCategory() {
    return category;
  }

}
