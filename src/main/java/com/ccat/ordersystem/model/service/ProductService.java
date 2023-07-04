package com.ccat.ordersystem.model.service;

import com.ccat.ordersystem.model.ProductCreateRequest;
import com.ccat.ordersystem.model.ProductResponse;
import com.ccat.ordersystem.model.entity.Product;
import com.ccat.ordersystem.model.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductCreateRequest request) {
        Product response = productRepository.save(new Product(
                UUID.randomUUID().getLeastSignificantBits() & Long.MAX_VALUE,
                request.name(),
                request.skuCode(),
                request.unitPrice()
        ));
        return mapToProductResponse(response);
    }

    public List<Product> getProductsById(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

    private static ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSkuCode(),
                product.getUnitPrice()
        );
    }
}
