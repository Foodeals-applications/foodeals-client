package net.foodeals.dlc.infrastructure.interfaces.web.mappers;

import net.foodeals.dlc.application.dtos.responses.DlcDto;
import net.foodeals.dlc.application.dtos.responses.UserProductResponse;
import net.foodeals.dlc.domain.entities.Dlc;
import net.foodeals.dlc.domain.enums.ValorisationType;

import java.util.Collections;

public class DlcMapper {
    public static DlcDto toDto(Dlc dlc) {
        DlcDto dto = new DlcDto();
        dto.setId(dlc.getId());
        dto.setProductName(dlc.getProduct().getName());
        dto.setExpiryDate(dlc.getExpiryDate());
        dto.setTimeRemaining(dlc.getTimeRemaining());
        dto.setQuantity(dlc.getQuantity());
        dto.setDiscount(dlc.getDiscount());
        dto.setValorisationType(dlc.getValorisationType());
        return dto;
    }

    public static UserProductResponse toUserProductResponse(Dlc dlc) {
        UserProductResponse r = new UserProductResponse();
        r.setId(dlc.getId());
        if (dlc.getProduct() != null) {
            r.setProductId(dlc.getProduct().getId());
            r.setName(dlc.getProduct().getName());
            r.setBarcode(dlc.getProduct().getBarcode());
            r.setImageUrls(Collections.singletonList(dlc.getProduct().getProductImagePath()));
        } else {
            r.setName(dlc.getMotif());
        }
        r.setQuantity(dlc.getQuantity());
        r.setExpiresAt(dlc.getExpiryDate());
        r.setStatus(mapStatus(dlc.getValorisationType()));
        r.setCreatedAt(null);
        r.setUpdatedAt(null);
        return r;
    }

    private static String mapStatus(ValorisationType v) {
        if (v == null) return "suitable";
        return switch (v) {
            case URGENTE -> "urgent";
            case EXIGEE -> "exige";
            case SOUHAITABLE -> "suitable";
        };
    }
}
