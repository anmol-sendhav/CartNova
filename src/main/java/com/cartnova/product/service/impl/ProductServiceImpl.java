package com.cartnova.product.service.impl;
import com.cartnova.product.dto.request.product.ProductRequest;
import com.cartnova.product.dto.response.product.ProductResponse;
import com.cartnova.product.entity.Product;
import com.cartnova.product.exception.ProductNotFoundException;
import com.cartnova.product.mapper.ProductMapper;
import com.cartnova.product.repository.ProductRepository;
import com.cartnova.product.repository.specification.ProductSpecification;
import com.cartnova.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public Page<ProductResponse> getAllProducts(
            int page,
            int size,
            String sortBy,
            String direction,
            String keyword,
            String category) {

        if (page < 0) {
            throw new IllegalArgumentException("Page must not be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }

        List<String> allowedSortFields = List.of(
                "id", "name", "price", "stockQuantity", "category"
        );

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field");
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Direction must be asc or desc");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Product> specification =
                ProductSpecification.filterProducts(keyword, category);

        Page<Product> products =
                productRepository.findAll(specification, pageable);

        return products.map(productMapper::mapToResponse);
    }
}