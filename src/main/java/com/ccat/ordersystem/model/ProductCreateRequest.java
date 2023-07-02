package com.ccat.ordersystem.model;

public record ProductCreateRequest(
        String name,
        String skuCode,
        long unitPrice
) { }
