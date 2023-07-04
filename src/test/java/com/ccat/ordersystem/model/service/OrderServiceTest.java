package com.ccat.ordersystem.model.service;

import com.ccat.ordersystem.model.OrderCreateRequest;
import com.ccat.ordersystem.model.OrderLineRequest;
import com.ccat.ordersystem.model.OrderResponse;
import com.ccat.ordersystem.model.entity.Customer;
import com.ccat.ordersystem.model.entity.Order;
import com.ccat.ordersystem.model.entity.OrderLine;
import com.ccat.ordersystem.model.entity.Product;
import com.ccat.ordersystem.model.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class OrderServiceTest {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final OrderLineService orderLineService;
    private final OrderService orderService;

    public OrderServiceTest() {
        this.orderRepository = mock(OrderRepository.class);
        this.customerService = mock(CustomerService.class);
        this.orderLineService = mock(OrderLineService.class);

        this.orderService = new OrderService(
                orderRepository,
                customerService,
                orderLineService
        );
    }

    private Order createOrder(int orderLineAmount, Long id) {
        List<OrderLine> orderLineList = new ArrayList<>();

        for (int i = 0; i < orderLineAmount; i++) {
            Product product = new Product(i, "Test", "SKU-"+i, 123);
            OrderLine orderLine = new OrderLine(product, 5);
            orderLineList.add(orderLine);
        }

        Customer customer = new Customer(
                id, "123", "123", "123", "123"
        );
        return new Order(
                id,
                orderLineList,
                customer,
                LocalDate.now()
        );
    }

    private OrderCreateRequest getRequestFromOrder(Order order) {
        List<OrderLineRequest> lineRequests = order.getOrderLineList().stream().map(o ->
                new OrderLineRequest(o.getProduct().getId(), o.getQuantity())
        ).toList();

        return new OrderCreateRequest(
                lineRequests,
                order.getCustomer().getId()
        );
    }

    @Test
    void createSingleOrder_returnsOrder() {
        //given
        Order order = createOrder(8, UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        OrderCreateRequest request = getRequestFromOrder(order);

        given(customerService.getCustomerById(request.customerId())).willReturn(order.getCustomer());
        given(orderLineService.getOrderLineItems(request.orderLineList())).willReturn(order.getOrderLineList());
        given(orderRepository.save(Mockito.any(Order.class))).willReturn(order);

        //when
        OrderResponse response = orderService.createOrder(request);

        //then
        assertEquals(OrderService.mapToOrderResponse(order), response);
    }

    @Test
    void getOrdersByProduct_And_OrdersByCustomer_returnsProducts_And_CustomerOrder() {
        //given
        long productsToCreate = 5;
        Long randomId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        Order order = createOrder((int)productsToCreate, randomId);

        given(orderRepository.findAllByProductId(productsToCreate)).willReturn(List.of(order));
        given(orderRepository.findAllByCustomerId(randomId)).willReturn(List.of(order));

        OrderResponse orderResponse = new OrderResponse(
                order.getId(),
                order.getOrderLineList(),
                order.getCustomer(),
                order.getDateOfSubmission()
        );

        //when
        List<OrderResponse> productsResponse = orderService.getOrdersByProduct(productsToCreate);
        List<OrderResponse> customerResponse = orderService.getOrdersByCustomer(randomId);

        //then
        assertEquals(List.of(orderResponse),productsResponse);
        assertEquals(List.of(orderResponse),customerResponse);
    }

    @Test
    void updateOrderLineQuantityByValidId_returnsIdOfUpdatedOrderLine() {
        //given
        Long id = 1L;
        int quantity = 1;
        given(orderRepository.updateOrderLineById(id, quantity)).willReturn(1);

        //when
        Long returnedId = orderService.updateOrderLineQuantityById(id, quantity);

        //then
        assertThat(returnedId).isEqualTo(id);
    }

    @Test
    void updateOrderLineQuantityWithInvalidId_throwsException() {
        //given
        Long id = 1L;
        int quantity = 1;
        given(orderRepository.updateOrderLineById(id, quantity)).willReturn(0);

        //then
        assertThrows(RuntimeException.class, () ->
                //when
                orderService.updateOrderLineQuantityById(id, quantity)
        );
    }

    @Test
    void updateOrderLineWithNegativeQuantity_throwsException() {
        //given
        Long id = 1L;
        int quantity = -10;
        given(orderRepository.updateOrderLineById(id, quantity)).willReturn(1);

        //then
        assertThrows(RuntimeException.class, () ->
                //when
                orderService.updateOrderLineQuantityById(id, quantity)
        );
    }
}