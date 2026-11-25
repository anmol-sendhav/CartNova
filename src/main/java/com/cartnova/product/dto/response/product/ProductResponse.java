package com.cartnova.product.dto.response.product;

import java.math.BigDecimal;

public record ProductResponse(

        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String category,
        String imageUrl

) {}
