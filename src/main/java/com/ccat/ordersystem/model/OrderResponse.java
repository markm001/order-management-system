package com.ccat.ordersystem.model;

import com.ccat.ordersystem.model.entity.Customer;
import com.ccat.ordersystem.model.entity.OrderLine;

import java.time.LocalDate;
import java.util.List;

public record OrderResponse (
    long id,
    List<OrderLine> orderLineList,
    Customer customer,
    LocalDate dateOfSubmission
){ }
