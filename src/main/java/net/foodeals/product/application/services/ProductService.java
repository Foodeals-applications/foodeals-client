package net.foodeals.product.application.services;


import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.NotFoundException;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;

public interface ProductService extends CrudService<Product, UUID, ProductRequest> {

    
	Page<Product> findProductsByName(String name,Pageable pageable);
	
	String saveFile(MultipartFile file);

	void deleteProduct(UUID id, String reason, String motif);

	Product applyDiscount(UUID productId, int discountPercentage);

	public Page<Product> searchProducts(String name, String brand, UUID categoryId, UUID subCategoryId,
			String barcode,Integer userId, Instant startDate, Instant endDate,Pageable pageable);

}

