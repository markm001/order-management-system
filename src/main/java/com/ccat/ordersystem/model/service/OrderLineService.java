package com.ccat.ordersystem.model.service;

import com.ccat.ordersystem.exception.InvalidIdException;
import com.ccat.ordersystem.model.OrderLineRequest;
import com.ccat.ordersystem.model.entity.OrderLine;
import com.ccat.ordersystem.model.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderLineService {
    private final ProductService productService;

    public OrderLineService(ProductService productService) {
        this.productService = productService;
    }

    public List<OrderLine> getOrderLineItems(List<OrderLineRequest> request) {
        //retrieve List of relevant Products
        List<Long> productIds = request.stream()
                .map(OrderLineRequest::productId)
                .toList();

        List<Product> productList = productService.getProductsById(productIds);

        return request.stream()
                .map(o -> mapToOrderLineResponse(productList, o))
                .toList();
    }

    private static OrderLine mapToOrderLineResponse(List<Product> productList, OrderLineRequest o) {
        return new OrderLine(
                productList.stream()
                        .filter(p -> p.getId() == o.productId())
                        .findFirst()
                        .orElseThrow(
                                new InvalidIdException("Product with ID:%d was not found.", o.productId())
                        ),
                o.quantity()
        );
    }
}
