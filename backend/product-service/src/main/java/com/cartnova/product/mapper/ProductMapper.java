package com.cartnova.product.mapper;

import com.cartnova.product.dto.request.product.ProductRequest;
import com.cartnova.product.dto.response.product.ProductResponse;
import com.cartnova.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product mapToEntity(ProductRequest request) {

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());
        return product;
    }

    public ProductResponse mapToResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getImageUrl()
        );
    }

    public void updateEntity(Product product, ProductRequest request) {

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());
    }
}
