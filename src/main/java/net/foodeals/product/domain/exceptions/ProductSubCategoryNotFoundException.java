package net.foodeals.product.domain.exceptions;

import java.util.UUID;

public class ProductSubCategoryNotFoundException extends RuntimeException {
    private final UUID id;

    public ProductSubCategoryNotFoundException(UUID id) {
        super("product sub category with id " + id + " not found");
        this.id = id;
    }
}
