package com.ccat.ordersystem.model.entity;


import jakarta.persistence.*;

@Entity
@Table(
        name = "products",
        uniqueConstraints = @UniqueConstraint(columnNames = "skuCode")
)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String skuCode;
    private long unitPrice;

    public Product() { }

    public Product(long id, String name, String skuCode, long unitPrice) {
        this.id = id;
        this.name = name;
        this.skuCode = skuCode;
        this.unitPrice = unitPrice;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public long getUnitPrice() {
        return unitPrice;
    }
}
