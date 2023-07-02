package com.ccat.ordersystem.model;

public record OrderLineRequest(
        Long productId,
        int quantity
) { }
