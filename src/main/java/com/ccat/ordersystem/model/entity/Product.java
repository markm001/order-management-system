package com.ccat.ordersystem.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String skuCode;
    private long unitPrice;
}
