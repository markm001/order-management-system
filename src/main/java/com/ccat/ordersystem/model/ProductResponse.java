package com.ccat.ordersystem.model;

public record ProductResponse(
        long id,
        String name,
        String skuCode,
        long unitPrice
) { }
