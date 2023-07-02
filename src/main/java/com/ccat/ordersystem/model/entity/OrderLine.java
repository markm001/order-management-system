package com.ccat.ordersystem.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class OrderLine implements Serializable {
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    private int quantity;

    public OrderLine() { }

    public OrderLine(Long id, Product product, int quantity) {
        this.id = id;
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
