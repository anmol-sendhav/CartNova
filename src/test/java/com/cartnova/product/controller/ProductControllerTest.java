package com.cartnova.product.controller;

import com.cartnova.product.dto.response.product.ProductResponse;
import com.cartnova.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void getProductById_shouldReturn200() throws Exception {

        ProductResponse response = new ProductResponse(
                1L,
                "Keyboard",
                "Mechanical keyboard",
                new BigDecimal("2500"),
                10,
                "Electronics",
                "https://example.com/keyboard.jpg"
        );

        when(productService.getProductById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Keyboard"))
                .andExpect(jsonPath("$.category").value("Electronics"));

        verify(productService).getProductById(1L);
    }

    @Test
    void createProduct_shouldReturn201() throws Exception {

        ProductResponse response = new ProductResponse(
                1L,
                "Keyboard",
                "Mechanical keyboard",
                new BigDecimal("2500"),
                10,
                "Electronics",
                "https://example.com/keyboard.jpg"
        );

        when(productService.createProduct(org.mockito.ArgumentMatchers.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Keyboard",
                                    "description": "Mechanical keyboard",
                                    "price": 2500,
                                    "stockQuantity": 10,
                                    "category": "Electronics",
                                    "imageUrl": "https://example.com/keyboard.jpg"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Keyboard"));
    }

    @Test
    void createProduct_shouldReturn400_whenNameIsEmpty() throws Exception {

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "",
                                    "description": "Mechanical keyboard",
                                    "price": 2500,
                                    "stockQuantity": 10,
                                    "category": "Electronics",
                                    "imageUrl": "https://example.com/keyboard.jpg"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}