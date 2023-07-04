package com.ccat.ordersystem.model.service;

import com.ccat.ordersystem.model.OrderLineRequest;
import com.ccat.ordersystem.model.entity.OrderLine;
import com.ccat.ordersystem.model.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class OrderLineServiceTest {
    private ProductService productService;
    private OrderLineService orderLineService;

    @BeforeEach
    void setUp() {
        this.productService = mock(ProductService.class);
        this.orderLineService = new OrderLineService(productService);
    }
    @Test
    void getOrderLineItemsWithInvalidProduct_throwsException() {
        //given
        long productId = 1L;
        List<OrderLineRequest> request = List.of(
                new OrderLineRequest(productId, 1)
        );

        given(productService.getProductsById(List.of(productId))
        ).willReturn(List.of());


        //then
        assertThrows(RuntimeException.class,() ->
                //when
                orderLineService.getOrderLineItems(request)
        );
    }

    @Test
    void getOrderLineItemsWithProduct_returnsListOfOrderLines() {
        //given
        List<Product> products = List.of(
                new Product(1L, "Product1", "123", 300),
                new Product(2L, "Product2", "345", 669),
                new Product(3L, "Product3", "567", 261)
        );

        List<OrderLine> expected = List.of(
                new OrderLine(products.get(0), 1),
                new OrderLine(products.get(1), 3),
                new OrderLine(products.get(2), 8)
        );

        List<OrderLineRequest> request = expected.stream()
                .map(o -> new OrderLineRequest(
                        o.getProduct().getId(),
                        o.getQuantity()
                ))
                .toList();
        List<Long> productIds = products.stream().map(Product::getId).toList();

        given(productService.getProductsById(productIds)
        ).willReturn(products);

        //when
        List<OrderLine> orderLines = orderLineService.getOrderLineItems(request);

        //then
        assertEquals(expected.size(), orderLines.size());
        assertThat(orderLines).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }
}