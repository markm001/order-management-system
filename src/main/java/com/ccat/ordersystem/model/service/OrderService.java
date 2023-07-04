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

    /**
     * Creates and saves an Order for the specified OrderCreateRequest Object
     * @param request OrderCreateRequest Object
     * @return the saved OrderResponse Object
     */
    public OrderResponse createOrder(OrderCreateRequest request) {
        Order savedOrder = createAndSaveOrder(request, LocalDate.now());

        return mapToOrderResponse(savedOrder);
    }

    /**
     * Helper Method for creating Orders for a specified Date
     * (Used for Test-Data)
     * @param request Entity to create
     * @param date Date of creation
     * @return OrderResponse Object
     */
    public OrderResponse createOrder(OrderCreateRequest request, String date) {
        LocalDate creationDate = LocalDate.parse(date);
        Order savedOrder = createAndSaveOrder(request, creationDate);

        return mapToOrderResponse(savedOrder);
    }

    /**
     * Returns all orders within a specified range
     * @param request OrderDateRequest Object
     * @return A List of OrderResponse Objects created between the specified dates.
     */
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

    /**
     * Finds a List of Orders containing the Product
     * @param productId Primary-Key of the Product Entity to search for
     * @return List of OrderResponse Objects or empty List, if none were found
     */
    public List<OrderResponse> getOrdersByProduct(Long productId) {
        return orderRepository
                .findAllByProductId(productId).stream()
                .map(OrderService::mapToOrderResponse)
                .toList();
    }

    /**
     * Finds a List of Orders issued for a specific Customer
     * @param customerId Primary-Key of the Customer Entity
     * @return List of OrderResponse Objects or empty List, if none were found
     */
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        return orderRepository
                .findAllByCustomerId(customerId).stream()
                .map(OrderService::mapToOrderResponse)
                .toList();
    }

    /**
     * Updates the Quantity in an Order Line with the new quantity
     * @param orderLineId Id of the Order Line to modify
     * @param quantity Quantity to set
     * @return The OrderLine-Id of the Order Line
     * @throws InvalidIdException If the OrderLine-Id was not found
     * @throws InvalidRequestException If a negative quantity was entered
     */
    public Long updateOrderLineQuantityById(Long orderLineId, int quantity) {
        if(quantity < 0)
            throw new InvalidRequestException("Unable to update OrderLine with negative quantity:%d", quantity);
        int success = orderRepository.updateOrderLineById(orderLineId, quantity);

        if(success == 0)
            throw new InvalidIdException("Unable to update OrderLine with Id:%d", orderLineId);

        return orderLineId;
    }

    /**
     * Finds a valid Customer, creates an OrderLine with valid Items and saves the Order Entity
     * @param request OrderCreateRequest Object
     * @param creationDate allows specifying a customer creation date for Testing (LocalDate.now() otherwise)
     * @return The saved Order Entity
     */
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

    /**
     * Maps the Order Entity to an OrderResponse Object
     * @param order OrderEntity to map
     * @return OrderResponse Object
     */
    public static OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderLineList(),
                order.getCustomer(),
                order.getDateOfSubmission()
        );
    }
}
