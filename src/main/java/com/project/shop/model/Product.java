package com.project.shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    String description;
    String manufacturer;
    double price;

    ProductType productType;

    @ManyToOne
    Warehouse warehouse;

    @ManyToOne
    Cart cart;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Review> reviews;

    public Product(String title, String description, String manufacturer, Warehouse warehouse, ProductType productType, double price) {
        this.title = title;
        this.description = description;
        this.manufacturer = manufacturer;
        this.warehouse = warehouse;
        this.productType = productType;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product: " + title + "\n" +
                "Price: " + price;
    }

}
