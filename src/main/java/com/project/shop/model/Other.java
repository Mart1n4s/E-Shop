package com.project.shop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Other extends Product {
    private double weight;

    public Other(String title, String description, String manufacturer, Warehouse warehouse, Double weight,
                 ProductType productType, double price){
        super(title, description, manufacturer, warehouse, productType, price);
        this.weight = weight;
    }
}
