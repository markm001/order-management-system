package com.ccat.ordersystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class OrderController {
    @PostMapping("/order")
    public String createOrder() {
        return "Hello World from OrderController";
    }

    @GetMapping("/order")
    public String searchOrdersByDate(LocalDate date) {
        return "Hello World from OrderController";
    }
}
