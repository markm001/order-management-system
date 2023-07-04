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

    /**
     * Retrieves a List of Products for the request and merges them into a List of OrderLine Entities
     * @param request OrderLineRequest Object
     * @return List of OrderLine Entities
     */
    public List<OrderLine> getOrderLineItems(List<OrderLineRequest> request) {
        List<Long> productIds = request.stream()
                .map(OrderLineRequest::productId)
                .toList();

        List<Product> productList = productService.getProductsById(productIds);

        return request.stream()
                .map(o -> mapToOrderLine(productList, o))
                .toList();
    }

    /**
     * Filters the List of Products for a specific Id from the OrderLineRequest and joins it into a valid OrderLineEntity
     * @param productList List of Products for each Id.
     * @param o OrderLineRequest Object
     * @return OrderLine Entity
     * @throws InvalidIdException If no Product was found for the Id.
     */
    private static OrderLine mapToOrderLine(List<Product> productList, OrderLineRequest o) {
        return new OrderLine(
                productList.stream()
                        .filter(p -> p.getId() == o.productId())
                        .findFirst()
                        .orElseThrow(() ->
                                new InvalidIdException("Product with ID:%d was not found.", o.productId())
                        ),
                o.quantity()
        );
    }
}
