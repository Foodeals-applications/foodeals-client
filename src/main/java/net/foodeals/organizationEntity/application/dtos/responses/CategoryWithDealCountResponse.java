package net.foodeals.organizationEntity.application.dtos.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class CategoryWithDealCountResponse {
	
	 private UUID categoryId;
	 private String categoryName;
	 private String photoUrl ;
	 private long dealCount;

}
