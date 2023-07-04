package com.ccat.ordersystem.controller;

import com.ccat.ordersystem.model.ProductCreateRequest;
import com.ccat.ordersystem.model.ProductResponse;
import com.ccat.ordersystem.model.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    public ProductResponse createProduct(@RequestBody ProductCreateRequest request) {
        return productService.createProduct(request);
    }
}
