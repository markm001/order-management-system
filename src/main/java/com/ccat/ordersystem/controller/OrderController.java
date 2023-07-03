package com.ccat.ordersystem.controller;

import com.ccat.ordersystem.model.OrderCreateRequest;
import com.ccat.ordersystem.model.OrderDateRequest;
import com.ccat.ordersystem.model.OrderResponse;
import com.ccat.ordersystem.model.ProductResponse;
import com.ccat.ordersystem.model.entity.Order;
import com.ccat.ordersystem.model.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/order/{date}")
    public Order createOrderWithDate(
            @RequestBody OrderCreateRequest request,
            @PathVariable String date
    ) {
        return orderService.createOrder(request, date);
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public List<OrderResponse> searchOrdersByDateProductOrCustomer(
            @RequestParam(required = false, name = "product") Optional<Long> productId,
            @RequestParam(required = false, name = "customer") Optional<Long> customerId,
            @RequestBody(required = false) OrderDateRequest request
    ) {
        if(request != null) {
            return orderService.getOrdersByDate(request);
        } else if (productId.isPresent()) {
            return orderService.getOrdersByProduct(productId.get());
        } else if (customerId.isPresent()) {
            return orderService.getOrdersByCustomer(customerId.get());
        }

        return List.of();
    }
}
