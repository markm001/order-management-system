package com.ccat.ordersystem.model.entity;

import jakarta.persistence.*;

@Embeddable
public class OrderLine {
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    private int quantity;
}
