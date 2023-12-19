package com.project.shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "", cascade = CascadeType.MERGE, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Product> itemsInCart;

    @ManyToOne
    private User owner;

    OrderStatus orderStatus;

    @ManyToOne
    private Manager employee;

    @OneToMany(mappedBy = "", cascade = CascadeType.MERGE, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> orderComments;

    public Cart(User owner, OrderStatus orderStatus, List<Product> itemsInCart) {
        this.owner = owner;
        this.dateCreated = LocalDate.now();
        this.orderStatus = orderStatus;
        this.itemsInCart = itemsInCart;
    }

    public Cart(List<Comment> orderComments) {
        this.orderComments = orderComments;
    }

    @Override
    public String toString() {
        return "Items in cart: " + itemsInCart + "\n" +
                "Customer: " + owner;
    }
}
