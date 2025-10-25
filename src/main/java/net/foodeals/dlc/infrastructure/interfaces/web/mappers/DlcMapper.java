package net.foodeals.dlc.infrastructure.interfaces.web.mappers;

import net.foodeals.dlc.application.dtos.requests.GlobalProductDto;
import net.foodeals.dlc.application.dtos.responses.DlcDto;
import net.foodeals.dlc.application.dtos.responses.ScanCreateResponse;
import net.foodeals.dlc.application.dtos.responses.UserProductResponse;
import net.foodeals.dlc.domain.entities.Dlc;
import net.foodeals.dlc.domain.enums.ValorisationType;
import net.foodeals.product.domain.entities.Product;

import java.util.Collections;
import java.util.Date;

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

        // id
        r.setId(dlc.getId());

        // userId : prendre le premier user si présent (adapter si tu veux une liste)
        if (dlc.getUsers() != null && !dlc.getUsers().isEmpty() && dlc.getUsers().get(0) != null) {
            r.setUserId(dlc.getUsers().get(0).getId().toString());
        } else {
            r.setUserId(null); // ou une valeur par défaut si souhaité
        }

        // product info
        if (dlc.getProduct() != null) {
            r.setProductId(dlc.getProduct().getId());
            r.setName(dlc.getProduct().getName());
            r.setBarcode(dlc.getProduct().getBarcode());
            // imageUrls safe
            String img = dlc.getProduct().getProductImagePath();
            r.setImageUrls(img != null ? Collections.singletonList(img) : Collections.emptyList());

            // price (ADAPT selon le nom de la méthode dans Price)
            try {
                if (dlc.getProduct().getPrice() != null) {
                    // supposons Price a getAmount() qui retourne BigDecimal
                    r.setPrice(dlc.getProduct().getPrice().amount().doubleValue());
                } else {
                    r.setPrice(null);
                }
            } catch (NoSuchMethodError | RuntimeException ex) {
                // Si ta classe Price utilise un autre getter (getValue, getPrice, etc.), adapte ici.
                r.setPrice(null);
            }
        } else {
            // produit "manuel"
            r.setName(dlc.getMotif() != null ? dlc.getMotif() : dlc.getReason());
            r.setImageUrls(Collections.emptyList());
            r.setProductId(null);
            r.setBarcode(null);
            r.setPrice(null);
        }

        // quantités / unité
        r.setQuantity(dlc.getQuantity());
        r.setUnit(r.getUnit() == null ? "pcs" : r.getUnit());

        // statut / valorisation
        r.setStatus(mapStatus(dlc.getValorisationType()));

        // notification flag (si champ présent dans Dlc sinon true par défaut)
        try {
            // si Dlc a getNotifyOnStatusChange()
            r.setNotifyOnStatusChange(dlc.getNotifyOnStatusChange() != null ? dlc.getNotifyOnStatusChange() : Boolean.TRUE);
        } catch (NoSuchMethodError | RuntimeException e) {
            r.setNotifyOnStatusChange(Boolean.TRUE);
        }

        // note / location (adapter noms de champs si différents)
        r.setNote(dlc.getReason() != null ? dlc.getReason() : dlc.getMotif());
        if (dlc.getProduct() != null && dlc.getProduct().getSubEntity() != null) {
            r.setLocation(dlc.getProduct().getSubEntity().getName());
        } else {
            r.setLocation(null);
        }


        // createdAt / updatedAt : conversion Instant -> Date attendu par DTO
        // ADAPT : si dlc.getCreatedAt() retourne Instant, on convertit ; si déjà Date, cast direct.
        try {
            if (dlc.getCreatedAt() != null) {
                // suppose getCreatedAt() retourne Instant
                r.setCreatedAt(Date.from(dlc.getCreatedAt()));
            } else {
                r.setCreatedAt(null);
            }
        } catch (ClassCastException ex) {
            // si getCreatedAt() retourne déjà Date
            try {
                r.setCreatedAt(Date.from(dlc.getCreatedAt()));
            } catch (ClassCastException ex2) {
                r.setCreatedAt(null);
            }
        } catch (Throwable t) {
            r.setCreatedAt(null);
        }

        try {
            if (dlc.getUpdatedAt() != null) {
                r.setUpdatedAt(Date.from(dlc.getUpdatedAt()));
            } else {
                r.setUpdatedAt(null);
            }
        } catch (ClassCastException ex) {
            try {
                r.setUpdatedAt(Date.from(dlc.getUpdatedAt()));
            } catch (ClassCastException ex2) {
                r.setUpdatedAt(null);
            }
        } catch (Throwable t) {
            r.setUpdatedAt(null);
        }

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
