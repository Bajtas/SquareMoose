package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "btproduct")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category")
//  @JsonManagedReference
  private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
   // @JsonBackReference
    private List<OrderItem> orderItems;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "btimage", joinColumns = {
          @JoinColumn(name = "product_id", nullable = false) },
          inverseJoinColumns = { @JoinColumn(name = "image_id", nullable = false) })
  //@JsonManagedReference
  private List<ProductImage> images;

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

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
