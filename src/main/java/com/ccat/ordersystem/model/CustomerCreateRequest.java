package com.ccat.ordersystem.model;

public record CustomerCreateRequest (
        String registrationCode,
        String fullName,
        String email,
        String telephone
){ }
