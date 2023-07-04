package com.ccat.ordersystem.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class OrderLine implements Serializable {
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    private int quantity;

    public OrderLine() { }

    public OrderLine(Product product, int quantity) {
        this.id = UUID.randomUUID().getMostSignificantBits()&Long.MAX_VALUE;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
