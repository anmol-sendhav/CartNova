package com.cartnova.product.dto.request.product;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "Product name is required")
        String name,

        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stockQuantity,

        @NotBlank(message = "Category is required")
        String category,

        String imageUrl

) {}
