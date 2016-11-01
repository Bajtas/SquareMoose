package pl.bajtas.squaremoose.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Bajtas on 03.09.2016.
 */

@Entity
@JsonIdentityInfo(generator=JSOGGenerator.class)
@Table(name = "btproductimage")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imageSrc;
    private Date addedOn;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "images")
    private List<Product> products;

    public ProductImage(String imageSrc, Date addedOn, List<Product> products) {
        this.imageSrc = imageSrc;
        this.addedOn = addedOn;
        this.products = products;
    }

    public ProductImage(String imageSrc, Date addedOn) {
        this.imageSrc = imageSrc;
        this.addedOn = addedOn;
    }

    public ProductImage(String imageSrc) {
        this.imageSrc = imageSrc;
        this.addedOn = new Date();
    }

    public ProductImage() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof ProductImage)) {
            return false;
        }

        ProductImage otherProductImage = (ProductImage) other;
        return id.equals(otherProductImage.getId());
    }

}
