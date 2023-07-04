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

    /**
     * Creates a Product Entity with the specified values and saves it.
     * @param request ProductCreateRequest
     * @return ProductResponse Object
     */
    public ProductResponse createProduct(ProductCreateRequest request) {
        Product response = productRepository.save(new Product(
                UUID.randomUUID().getLeastSignificantBits() & Long.MAX_VALUE,
                request.name(),
                request.skuCode(),
                request.unitPrice()
        ));
        return mapToProductResponse(response);
    }

    /**
     * Finds a List of Products their Product-Ids
     * @param productIds List containing the desired Product Primary-Keys to find
     * @return List of Product Entities
     */
    public List<Product> getProductsById(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

    /**
     * Maps a Product Entity to a Response Object
     * @param product Product Entity to be mapped
     * @return Product Response
     */
    private static ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSkuCode(),
                product.getUnitPrice()
        );
    }
}
