package net.foodeals.dlc.infrastructure.interfaces.web.mappers;

import net.foodeals.dlc.application.dtos.requests.GlobalProductDto;
import net.foodeals.dlc.application.dtos.responses.DlcDto;
import net.foodeals.dlc.application.dtos.responses.ScanCreateResponse;
import net.foodeals.dlc.application.dtos.responses.UserProductResponse;
import net.foodeals.dlc.domain.entities.Dlc;
import net.foodeals.dlc.domain.enums.ValorisationType;
import net.foodeals.product.domain.entities.Product;

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

    public static GlobalProductDto toGlobalProductDto(Product p) {
        GlobalProductDto g = new GlobalProductDto();
        g.setId(p.getId());
        g.setBarcode(p.getBarcode());
        g.setName(p.getName());
        g.setImageUrls(Collections.singletonList(p.getProductImagePath()));
        return g;
    }


    public static ScanCreateResponse toScanCreateResponse(Dlc dlc) {
        ScanCreateResponse s = new ScanCreateResponse();
        s.setId(dlc.getId());
        s.setProductId(dlc.getProduct() != null ? dlc.getProduct().getId() : null);
        s.setName(dlc.getProduct() != null ? dlc.getProduct().getName() : null);
        s.setBarcode(dlc.getProduct() != null ? dlc.getProduct().getBarcode() : null);
        s.setQuantity(dlc.getQuantity());
        s.setUnit("pcs");
        s.setStatus(mapStatus(dlc.getValorisationType()));
        s.setCreatedAt(null);
        s.setUpdatedAt(null);
        return s;
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
