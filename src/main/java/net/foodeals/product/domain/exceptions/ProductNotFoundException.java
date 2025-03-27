package net.foodeals.product.domain.exceptions;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    private  UUID id;
    private  String barcode;

    public ProductNotFoundException(UUID id) {
        super("product with id " + id + " not found");
        this.id = id;
    }
    
    public ProductNotFoundException(String barcode) {
        super("product with barcode " + barcode + " not found");
        this.barcode = barcode;
    }
}

