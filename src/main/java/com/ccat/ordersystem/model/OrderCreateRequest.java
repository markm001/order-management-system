package com.ccat.ordersystem.model;

import java.util.List;

public record OrderCreateRequest (
    List<OrderLineRequest> orderLineList,
    Long customerId
){ }
