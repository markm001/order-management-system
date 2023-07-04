package com.ccat.ordersystem.model.service;

import com.ccat.ordersystem.exception.InvalidDateException;
import com.ccat.ordersystem.exception.InvalidIdException;
import com.ccat.ordersystem.exception.InvalidRequestException;
import com.ccat.ordersystem.model.OrderCreateRequest;
import com.ccat.ordersystem.model.OrderDateRequest;
import com.ccat.ordersystem.model.OrderResponse;
import com.ccat.ordersystem.model.entity.Customer;
import com.ccat.ordersystem.model.entity.Order;
import com.ccat.ordersystem.model.entity.OrderLine;
import com.ccat.ordersystem.model.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    public OrderResponse createOrder(OrderCreateRequest request) {
        Order savedOrder = createAndSaveOrder(request, LocalDate.now());

        return mapToOrderResponse(savedOrder);
    }

    /**
     * Helper Method for creating Orders for a specified Date
     * (Use for Test-Data)
     * @param request Entity to create
     * @param date Date of creation
     * @return OrderResponse Object
     */
    public OrderResponse createOrder(OrderCreateRequest request, String date) {
        LocalDate creationDate = LocalDate.parse(date);
        Order savedOrder = createAndSaveOrder(request, creationDate);

        return mapToOrderResponse(savedOrder);
    }

    public List<OrderResponse> getOrdersByDate(OrderDateRequest request) {
        try {
            LocalDate lower = LocalDate.parse(request.lowerBound());
            LocalDate upper = LocalDate.parse(request.upperBound());

            return orderRepository
                    .findAllByDateOfSubmission(lower, upper).stream()
                    .map(OrderService::mapToOrderResponse)
                    .toList();

        } catch (DateTimeParseException e) {
            throw new InvalidDateException("Invalid dates:%s - %s entered.",
                    request.lowerBound(),request.upperBound()
            );
        }
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

    public Long updateOrderLineQuantityById(Long orderLineId, int quantity) {
        if(quantity < 0)
            throw new InvalidRequestException("Unable to update OrderLine with negative quantity:%d", quantity);
        int success = orderRepository.updateOrderLineById(orderLineId, quantity);

        if(success == 0)
            throw new InvalidIdException("Unable to update OrderLine with Id:%d", orderLineId);

        return orderLineId;
    }

    private Order createAndSaveOrder(OrderCreateRequest request, LocalDate creationDate) {
        Customer customer = customerService.getCustomerById(request.customerId());
        List<OrderLine> orderLineList = orderLineService.getOrderLineItems(request.orderLineList());

        return orderRepository.save(new Order(
                UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE,
                orderLineList,
                customer,
                creationDate
        ));
    }

    public static OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderLineList(),
                order.getCustomer(),
                order.getDateOfSubmission()
        );
    }
}
