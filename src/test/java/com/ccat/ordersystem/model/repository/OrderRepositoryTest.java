package com.ccat.ordersystem.model.repository;

import com.ccat.ordersystem.model.entity.Customer;
import com.ccat.ordersystem.model.entity.Order;
import com.ccat.ordersystem.model.entity.OrderLine;
import com.ccat.ordersystem.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(locations="classpath:application-test.properties")
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    private static Customer createCustomer() {
        return new Customer(
                UUID.randomUUID().getMostSignificantBits(),
                UUID.randomUUID().toString(),
                "Test Person",
                "tst@gm.com",
                "0123456789"
        );
    }
    private static Product createProduct() {
        return new Product(
                UUID.randomUUID().getMostSignificantBits(),
                "Test-Product",
                UUID.randomUUID().toString(),
                3900
        );
    }
    private static OrderLine createOrderLine(Product product) {
        return new OrderLine(
                product,
                1
        );
    }
    private Order createOrder(Customer customer, Product product, LocalDate date) {
        Product savedProduct = productRepository.save(product);


        Order order = new Order(
                UUID.randomUUID().getMostSignificantBits(),
                List.of(createOrderLine(savedProduct)),
                customer,
                date
        );

        return orderRepository.save(order);
    }

    @Test
    void findAllOrdersForProductId_returnListWithOneOrder() {
        //given
        Customer customer = createCustomer();
        Customer savedCustomer = customerRepository.save(customer);
        Product product = createProduct();
        Order order = createOrder(savedCustomer, product, LocalDate.now());

        long productId = order.getOrderLineList().get(0).getProduct().getId();

        //when
        List<Order> ordersForProductId = orderRepository.findAllByProductId(productId);

        //then
        assertEquals(1, ordersForProductId.size());
        assertTrue(
                ordersForProductId.stream().anyMatch(p ->
                        p.getCustomer().getRegistrationCode().equals(customer.getRegistrationCode())
                ));
        assertEquals(order, ordersForProductId.get(0));
    }

    @Test
    void findAllOrdersForCustomerId_returnListWithAllCustomerOrders() {
        //given
        Customer customer = customerRepository.save(createCustomer());

        List<Order> orderList = List.of(
                createOrder(customer, createProduct(), LocalDate.now()),
                createOrder(customer, createProduct(), LocalDate.now()),
                createOrder(customer, createProduct(), LocalDate.now())
        );

        long customerId = customer.getId();

        //when
        List<Order> ordersForCustomerId = orderRepository.findAllByCustomerId(customerId);

        //then
        assertEquals(orderList.size(), ordersForCustomerId.size());
        assertEquals(orderList, ordersForCustomerId);
    }

    @Test
    void findAllOrdersForMissingCustomerId_returnListWithNoOrders() {
        //given
        long customerId = 1L;

        //when
        List<Order> ordersForCustomerId = orderRepository.findAllByCustomerId(customerId);

        //then
        assertTrue(ordersForCustomerId.isEmpty());
    }

    @Test
    void findAllOrdersByDateOfSubmission_returnsListWithTwoDates() {
        //given
        Customer customer = customerRepository.save(createCustomer());

        LocalDate upperBound = LocalDate.parse("2023-07-03");
        LocalDate lowerBound = LocalDate.parse("2023-05-03");
        LocalDate excluded = LocalDate.parse("2021-05-03");

        List<Order> orderList = List.of(
                createOrder(customer, createProduct(), upperBound),
                createOrder(customer, createProduct(), lowerBound),
                createOrder(customer, createProduct(), excluded)
        );

        //when
        List<Order> ordersForDate = orderRepository.findAllByDateOfSubmission(lowerBound, upperBound);

        //then
        assertEquals(orderList.size() - 1, ordersForDate.size());
        assertTrue(ordersForDate.stream()
                .noneMatch(o -> o.getDateOfSubmission().equals(excluded))
        );
    }

    @Test
    void findAllOrdersWithBetweenDateOfSubmission_returnsOneOrder() {
        //given
        Customer customer = customerRepository.save(createCustomer());

        LocalDate upperBound = LocalDate.parse("2099-07-03");
        LocalDate lowerBound = LocalDate.parse("2070-05-03");

        Order savedOrder = createOrder(customer, createProduct(), LocalDate.parse("2081-02-13"));

        //when
        List<Order> ordersForDate = orderRepository.findAllByDateOfSubmission(lowerBound, upperBound);

        //then
        assertEquals(1, ordersForDate.size());
        assertEquals(savedOrder.getDateOfSubmission(), ordersForDate.get(0).getDateOfSubmission());
    }

    @Test
    void updateOrderLineById_returnsOneAffectedRow() {
        //given
        int updateQuantity = 77;
        Customer customer = customerRepository.save(createCustomer());
        Product product = createProduct();

        Product savedProduct = productRepository.save(product);
        OrderLine orderLine = createOrderLine(savedProduct);
        Order order = new Order(
                UUID.randomUUID().getMostSignificantBits(),
                List.of(orderLine),
                customer,
                LocalDate.now()
        );
        orderRepository.save(order);

        //when
        int rows = orderRepository.updateOrderLineById(orderLine.getId(), updateQuantity);

        //then
        assertEquals(1, rows);
    }

    @Test
    void updateOrderLineByInvalidId_returnsNoRows() {
        //given
        Long invalidId = 99L;

        //when
        int rows = orderRepository.updateOrderLineById(invalidId, 5);

        //then
        assertEquals(0, rows);
    }
}