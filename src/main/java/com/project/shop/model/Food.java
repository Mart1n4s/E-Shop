package com.project.shop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Food extends Product {

    public LocalDate expiryDate;

    public Food(String title, String description, String manufacturer, Warehouse warehouse, LocalDate expiryDate,
                ProductType productType, double price) {
        super(title, description, manufacturer, warehouse, productType, price);
        this.expiryDate = expiryDate;
    }



}
