package com.ccat.ordersystem.model.service;

import com.ccat.ordersystem.model.OrderCreateRequest;
import com.ccat.ordersystem.model.OrderDateRequest;
import com.ccat.ordersystem.model.OrderResponse;
import com.ccat.ordersystem.model.entity.Customer;
import com.ccat.ordersystem.model.entity.Order;
import com.ccat.ordersystem.model.entity.OrderLine;
import com.ccat.ordersystem.model.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
        Customer customer = customerService.getCustomerById(request.customerId());
        List<OrderLine> orderLineList = orderLineService.getOrderLineItems(request.orderLineList());

        return orderRepository.save(new Order(
                UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE,
                orderLineList,
                customer,
                LocalDate.now()
        ));
    }

    public Order createOrder(OrderCreateRequest request, String date) {
        Customer customer = customerService.getCustomerById(request.customerId());
        List<OrderLine> orderLineList = orderLineService.getOrderLineItems(request.orderLineList());

        LocalDate creationDate = LocalDate.parse(date);

        return orderRepository.save(new Order(
                UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE,
                orderLineList,
                customer,
                creationDate
        ));
    }

    public List<OrderResponse> getOrdersByDate(OrderDateRequest request) {
        LocalDate lower = LocalDate.parse(request.lowerBound());
        LocalDate upper = LocalDate.parse(request.upperBound());

        return orderRepository
                .findAllByDateOfSubmission(lower, upper).stream()
                .map(OrderService::mapToOrderResponse)
                .toList();
    }

    public List<OrderResponse> getOrdersByProduct(Long productId) {
        return orderRepository
                .findAllByProductId(productId).stream()
                .map(OrderService::mapToOrderResponse)
                .toList();
    }

    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        return orderRepository
                .findAllByCustomerId(customerId).stream()
                .map(OrderService::mapToOrderResponse)
                .toList();
    }

    private static OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderLineList(),
                order.getCustomer(),
                order.getDateOfSubmission()
        );
    }
}
