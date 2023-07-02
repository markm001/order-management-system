package com.ccat.ordersystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @PostMapping("/customer")
    public String createCustomer() {
        return "Hello World from CustomerController";
    }
}
