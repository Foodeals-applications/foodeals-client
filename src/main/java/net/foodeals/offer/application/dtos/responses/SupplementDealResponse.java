package net.foodeals.offer.application.dtos.responses;


import net.foodeals.common.valueOjects.Price; // adapte le package si différent
import net.foodeals.product.domain.entities.Supplement;
import net.foodeals.product.domain.enums.SupplementCategory;

import java.util.UUID;

public record SupplementDealResponse(
        UUID id,
        String name,
        Price price,
        String image,
        SupplementCategory supplementCategory
) {
    public static SupplementDealResponse fromEntity(Supplement s) {
        if (s == null) return null;

        // PRICE
        // j'assume que SupplementDeal#getPrice() retourne déjà un objet Price.
        // Si dans ton entity c'est un double/BigDecimal, adapte la conversion ici.
        Price price = null;
        try {
            price = s.getPrice();
        } catch (NoSuchMethodError | NullPointerException e) {
            // si getPrice() n'existe pas ou est null, laisse price à null.
            // Si tu stockes le prix comme Double dans l'entité, fais par ex.:
            // price = new Price(s.getPriceDouble()); // selon ton constructeur Price
        }

        // IMAGE
        // selon ton entity le getter peut être getImage(), getImagePath() ou getPhotoPath()
        String image = null;
        try {
            image = s.getSupplementImagePath();
        } catch (NoSuchMethodError ex1) {
            try { image = s.getSupplementImagePath(); } catch (NoSuchMethodError ex2) {
                try { image = s.getSupplementImagePath(); } catch (NoSuchMethodError ex3) {
                    image = null;
                }
            }
        }

        // CATEGORY
        // j'assume getSupplementCategory() ou getCategory()
        SupplementCategory category = null;
        try {
            category = s.getSupplementCategory();
        } catch (NoSuchMethodError ex1) {
            try { category = s.getSupplementCategory(); } catch (NoSuchMethodError ex2) {
                category = null;
            }
        }

        return new SupplementDealResponse(
                s.getId(),
                s.getName(),
                price,
                image,
                category
        );
    }
}
