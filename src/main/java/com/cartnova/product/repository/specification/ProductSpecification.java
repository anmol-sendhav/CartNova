package com.cartnova.product.repository.specification;

import com.cartnova.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> filterProducts(
            String keyword, String category) {

        return (root, query, criteriaBuilder) -> {

            var predicates = criteriaBuilder.conjunction();

            if (keyword != null && !keyword.isBlank()) {

                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("name")),
                                        "%" + keyword.toLowerCase() + "%"
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("description")),
                                        "%" + keyword.toLowerCase() + "%"
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

            return predicates;
        };
    }
}