package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.Builder;
import lombok.Data;
import net.foodeals.core.domain.entities.Name;

@Data
@Builder
public class ResponsibleInfoDto {
    private Name name;

    private String avatarPath;

    private String phone;

    private String email;
}
