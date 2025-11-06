package net.foodeals.product.application.specs;

import net.foodeals.core.domain.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductSpecs {
public static Specification<Product> inStore(UUID storeId) {
    return (root, query, cb) -> cb.equal(root.get("subEntity").get("id"), storeId);
}


public static Specification<Product> nameLike(String q) {
    return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%");
}


public static Specification<Product> categoryIn(List<UUID> categoryIds) {
    return (root, query, cb) -> root.get("category").get("id").in(categoryIds);
}


public static Specification<Product> domainEquals(UUID domainId) {
    return (root, query, cb) -> cb.equal(root.get("domain").get("id"), domainId);
}


public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
    return (root, query, cb) -> cb.between(root.get("price").get("newPrice"), min, max);
}


public static Specification<Product> onlyAvailable(boolean onlyAvailable) {
    return (root, query, cb) -> cb.greaterThan(root.get("stock"), 0);
}}