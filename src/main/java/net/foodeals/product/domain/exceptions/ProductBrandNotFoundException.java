package net.foodeals.product.domain.exceptions;

import java.util.UUID;

public class ProductBrandNotFoundException extends RuntimeException {
    private  UUID id;
    private  String name;

    public ProductBrandNotFoundException(UUID id) {
        super("product brand with id " + id + " not found");
        this.id = id;
    }
    
    public ProductBrandNotFoundException(String name) {
        super("product brand with name " + name + " not found");
        this.name = name;
    }
}
