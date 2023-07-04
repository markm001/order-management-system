package com.ccat.ordersystem.model;

public record CustomerResponse(
        long id,
        String registrationCode,
        String fullName,
        String email,
        String telephone
) { }
