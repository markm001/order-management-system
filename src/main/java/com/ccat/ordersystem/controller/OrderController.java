package com.ccat.ordersystem.controller;

import com.ccat.ordersystem.model.OrderCreateRequest;
import com.ccat.ordersystem.model.entity.Order;
import com.ccat.ordersystem.model.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public Order createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/order")
    public String searchOrdersByDate(LocalDate date) {
        return "Hello World from OrderController";
    }
}
