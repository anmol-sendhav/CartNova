package com.cartnova.product.service.impl;

import com.cartnova.product.entity.Product;
import com.cartnova.product.repository.ProductRepository;
import com.cartnova.product.mapper.ProductMapper;
import com.cartnova.product.dto.response.product.ProductResponse;
import com.cartnova.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {

        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Keyboard");
        product.setPrice(new BigDecimal("2500"));

        ProductResponse response = new ProductResponse(
                1L,
                "Keyboard",
                "Mechanical keyboard",
                new BigDecimal("2500"),
                10,
                "Electronics",
                "https://example.com/keyboard.jpg"
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productMapper.mapToResponse(product))
                .thenReturn(response);

        // Act
        ProductResponse result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Keyboard", result.name());
        assertEquals(new BigDecimal("2500"), result.price());

        verify(productRepository).findById(1L);
        verify(productMapper).mapToResponse(product);
    }

    @Test
    void getProductById_shouldThrowException_whenProductDoesNotExist() {

        // Arrange
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(999L)
        );

        verify(productRepository).findById(999L);
        verify(productMapper, never()).mapToResponse(any());
    }
}
