package net.foodeals.dlc.infrastructure.modelMapperConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.foodeals.dlc.application.dtos.responses.DlcResponse;
import net.foodeals.dlc.domain.entities.Dlc;
import net.foodeals.product.application.dtos.responses.PickupConditionResponse;
import net.foodeals.product.application.dtos.responses.ProductBrandResponse;
import net.foodeals.product.application.dtos.responses.ProductCategoryResponse;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.application.dtos.responses.ProductSubCategoryResponse;
import net.foodeals.product.application.dtos.responses.RayonResponse;
import net.foodeals.product.domain.entities.PickupCondition;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.ProductBrand;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.ProductSubCategory;
import net.foodeals.product.domain.entities.Rayon;
import net.foodeals.user.application.dtos.responses.UserResponse;

@Configuration
@RequiredArgsConstructor
public class DlcModelMapperConfig {

    private final ModelMapper mapper;

    @PostConstruct
    public void configure() {
        mapper.addConverter(context -> {
            ProductCategory category = context.getSource();
            return new ProductCategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                null
            );
        }, ProductCategory.class, ProductCategoryResponse.class);

        mapper.addConverter(context -> {
            ProductSubCategory category = context.getSource();
            return new ProductSubCategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug()
            );
        }, ProductSubCategory.class, ProductSubCategoryResponse.class);

        mapper.addConverter(context -> {
            ProductBrand brand = context.getSource();
            return new ProductBrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getSlug()
            );
        }, ProductBrand.class, ProductBrandResponse.class);

        mapper.addConverter(context -> {
            Rayon rayon = context.getSource();
            return new RayonResponse(
                rayon.getId(),
                rayon.getName()
            );
        }, Rayon.class, RayonResponse.class);

        mapper.addConverter(context -> {
            Product product = context.getSource();
            ProductCategoryResponse productCategory=null;
            ProductSubCategoryResponse productSubCategory=null;
           
            if(product.getCategory()!=null) {
            	productCategory= mapper.map(product.getCategory(), ProductCategoryResponse.class);
            }
            
            if(product.getSubcategory()!=null) {
            	  mapper.map(product.getSubcategory(), ProductSubCategoryResponse.class);
            }
            
            List<PickupConditionResponse> pickupConditions=new ArrayList<PickupConditionResponse>();
            if(product.getPickupConditions()!=null) {
            	for(PickupCondition pickupCondition:product.getPickupConditions()) {
            		PickupConditionResponse pickupConditionResponse=
            				new PickupConditionResponse(pickupCondition.getId(), 
            						pickupCondition.getConditionName(), 
            						pickupCondition.getDaysBeforePickup(), pickupCondition.getDiscountPercentage());
            		pickupConditions.add(pickupConditionResponse);
            	}
            }
            
            Date creationDate = product.getCreatedAt() != null ? Date.from(product.getCreatedAt()) : null;
            
            UserResponse createdBy=null;
			if(product.getCreatedBy()!=null) {
				createdBy=mapper.map(product.getCreatedBy(), UserResponse.class);
			}
			
            return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getTitle(),
                product.getDescription(),
                product.getProductImagePath(),
                productCategory,
                productSubCategory,
                product.getBrand(),
                product.getRayon(),
                product.getBarcode(),
                product.getType(),
                creationDate, 
                pickupConditions, null,null, createdBy
            );
        }, Product.class, ProductResponse.class);

        mapper.addConverter(context -> {
            Dlc dlc = context.getSource();
            return new DlcResponse(
                dlc.getId(),
                mapper.map(dlc.getProduct(), ProductResponse.class),
                dlc.getExpiryDate(),
                dlc.getQuantity(),
                dlc.getTimeRemaining()
            );
        }, Dlc.class, DlcResponse.class);
    }
}