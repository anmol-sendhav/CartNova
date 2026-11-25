package com.cartnova.product.service;

import com.cartnova.product.dto.request.product.ProductRequest;
import com.cartnova.product.dto.response.product.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public ProductResponse createProduct(ProductRequest request);
    public ProductResponse getProductById(Long id);
    public ProductResponse updateProduct(Long id, ProductRequest request);
    public void deleteProduct(Long id);
    Page<ProductResponse> getAllProducts(
            int page,
            int size,
            String sortBy,
            String direction,
            String keyword,
            String category
    );
}
