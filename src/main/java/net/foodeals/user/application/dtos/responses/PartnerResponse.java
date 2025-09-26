package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.foodeals.organizationEntity.domain.entities.SubEntity;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PartnerResponse {
    private UUID id;
    private String name;
    private String avatarPath;
    private String coverPath;

    public static PartnerResponse fromEntity(SubEntity subEntity) {
        return new PartnerResponse(
                subEntity.getId(),
                subEntity.getName(),
                subEntity.getAvatarPath(),
                subEntity.getCoverPath()
        );
    }
}
