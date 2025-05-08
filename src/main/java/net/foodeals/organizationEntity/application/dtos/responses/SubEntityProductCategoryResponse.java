package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubEntityProductCategoryResponse {

    private UUID id ;

    
    private String photoUrl;

    private String name;
}
