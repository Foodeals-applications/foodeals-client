package net.foodeals.common.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceUtils {
public static int calculatePercentageReduction(BigDecimal price, BigDecimal salePrice) {
    if (price == null || salePrice == null || price.compareTo(BigDecimal.ZERO) == 0) {
        return 0; // pour éviter la division par zéro
    }

    BigDecimal reduction = price.subtract(salePrice);
    BigDecimal percentage = reduction
            .divide(price, 4, RoundingMode.HALF_UP) // division précise à 4 décimales
            .multiply(BigDecimal.valueOf(100));

    return percentage.setScale(0, RoundingMode.HALF_UP).intValue(); // arrondi à l'entier le plus proche
}}