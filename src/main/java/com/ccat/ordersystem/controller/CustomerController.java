package com.ccat.ordersystem.controller;

import com.ccat.ordersystem.model.CustomerCreateRequest;
import com.ccat.ordersystem.model.CustomerResponse;
import com.ccat.ordersystem.model.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customer")
    public CustomerResponse createCustomer(@RequestBody CustomerCreateRequest request) {
        return customerService.createCustomer(request);
    }
}
