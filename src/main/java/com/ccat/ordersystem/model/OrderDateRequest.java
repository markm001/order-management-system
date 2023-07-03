package com.ccat.ordersystem.model;

public record OrderDateRequest(
    String lowerBound,
    String upperBound
){ }
