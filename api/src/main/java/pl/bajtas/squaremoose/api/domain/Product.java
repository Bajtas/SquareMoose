package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@JsonIdentityInfo(generator=JSOGGenerator.class)
@Table(name = "btproduct")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category")
    private Category category;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<OrderItem> orderItems;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "btimage", joinColumns = {@JoinColumn(name = "product_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "image_id", nullable = false)})
    private List<ProductImage> images;
    private String name;
    private String description;
    private double price;
    private Date addedOn;
    private Date lmod;

    public Product(String name, String description, double price, Date addedOn, Date lmod, Category category, List<ProductImage> images, List<OrderItem> orderItems) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.addedOn = addedOn;
        this.lmod = lmod;
        this.category = category;
        this.images = images;
        this.orderItems = orderItems;
    }

    public Product(String name, String description, double price, Date addedOn, Date lmod, Category category, List<ProductImage> images) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.addedOn = addedOn;
        this.lmod = lmod;
        this.category = category;
        this.images = images;
    }

    public Product(String name, String description, double price, Date addedOn, Date lmod, List<ProductImage> images) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.addedOn = addedOn;
        this.lmod = lmod;
        this.images = images;
    }

    public Product(String name, String description, double price, Date addedOn, Date lmod) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.addedOn = addedOn;
        this.lmod = lmod;
    }

    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.addedOn = new Date();
        this.lmod = new Date();
    }

    public Product() {

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

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
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

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Product)) {
            return false;
        }

        Product otherProduct = (Product) other;
        return id.equals(otherProduct.getId());
    }
}
