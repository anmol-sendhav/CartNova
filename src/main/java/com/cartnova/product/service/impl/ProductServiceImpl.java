package com.cartnova.product.service.impl;
import com.cartnova.product.dto.request.product.ProductRequest;
import com.cartnova.product.dto.response.product.ProductResponse;
import com.cartnova.product.entity.Product;
import com.cartnova.product.exception.ProductNotFoundException;
import com.cartnova.product.mapper.ProductMapper;
import com.cartnova.product.repository.ProductRepository;
import com.cartnova.product.service.ProductService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Product product = productMapper.mapToEntity(request);

        Product savedProduct = productRepository.save(product);

        return productMapper.mapToResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(productMapper::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with ID: " + id
                        ));

        return productMapper.mapToResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with ID: " + id
                        ));

        productMapper.updateEntity(product, request);

        Product updatedProduct = productRepository.save(product);

        return productMapper.mapToResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
        .orElseThrow(() ->
        new ProductNotFoundException("Product not found with ID: " + id));
        productRepository.delete(product);
    }
}