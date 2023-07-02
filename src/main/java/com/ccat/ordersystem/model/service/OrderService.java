package com.ccat.ordersystem.model.service;

import com.ccat.ordersystem.model.OrderCreateRequest;
import com.ccat.ordersystem.model.entity.Customer;
import com.ccat.ordersystem.model.entity.Order;
import com.ccat.ordersystem.model.entity.OrderLine;
import com.ccat.ordersystem.model.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final OrderLineService orderLineService;

    public OrderService(
            OrderRepository orderRepository,
            CustomerService customerService,
            OrderLineService orderLineService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.orderLineService = orderLineService;
    }

    public Order createOrder(OrderCreateRequest request) {
        Optional<Customer> customerResponse = customerService.getCustomerById(request.customerId());
        List<OrderLine> orderLineList = orderLineService.getOrderLineItems(request.orderLineList());

        return orderRepository.save(new Order(
                UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE,
                orderLineList,
                customerResponse.orElseThrow(),
                LocalDate.now()
        ));
    }
}
