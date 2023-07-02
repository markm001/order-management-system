package com.ccat.ordersystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @PostMapping("/product")
    public String createProduct() {
        return "Hello World from ProductController";
    }
}
