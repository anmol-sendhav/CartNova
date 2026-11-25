package com.cartnova.product.service;

import com.cartnova.product.dto.request.product.ProductRequest;
import com.cartnova.product.dto.response.product.ProductResponse;

import java.util.List;

public interface ProductService {

    public ProductResponse createProduct(ProductRequest request);
    public List<ProductResponse> getAllProducts();
    public ProductResponse getProductById(Long id);
    public ProductResponse updateProduct(Long id, ProductRequest request);
    public void deleteProduct(Long id);

}
