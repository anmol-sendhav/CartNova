package com.cartnova.product.repository.specification;

import com.cartnova.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> filterProducts(
            String keyword,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        return (root, query, criteriaBuilder) -> {

            var predicates = criteriaBuilder.conjunction();

            if (keyword != null && !keyword.isBlank()) {

                String searchKeyword = "%" + keyword.toLowerCase() + "%";

                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("name")),
                                        searchKeyword
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("description")),
                                        searchKeyword
                                )
                        )
                );
            }

            if (category != null && !category.isBlank()) {

                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("category")),
                                category.toLowerCase()
                        )
                );
            }

            if (minPrice != null) {

                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("price"),
                                minPrice
                        )
                );
            }

            if (maxPrice != null) {

                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("price"),
                                maxPrice
                        )
                );
            }

            return predicates;
        };
    }
}