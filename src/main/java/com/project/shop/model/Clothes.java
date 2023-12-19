package com.project.shop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Clothes extends Product {
    private String size;
    private String color;

    public Clothes(String title, String description, String manufacturer, Warehouse warehouse, String size, String color,
                   ProductType productType, double price){
        super(title, description, manufacturer, warehouse, productType, price);
        this.size = size;
        this.color = color;
    }
}
