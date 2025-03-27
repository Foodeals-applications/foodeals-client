package net.foodeals.offer.domain.enums;

import java.util.ArrayList;
import java.util.List;

public enum PublishAs {
    BAKERIES_PASTRIES("Bakeries and Pastries"),
    SUPERMARKETS_HYPERMARKETS("Supermarkets and Hypermarkets"),
    RESTAURANTS_HOTELS_CATERERS("Restaurants, Hotels, and Caterers"),
    WHOLESALERS_INDUSTRIALS("Wholesalers and Industrials");

    private final String description;

    PublishAs(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<PublishAsPair> getPublishAsPairs() {
        List<PublishAsPair> pairs = new ArrayList<>();
        for (PublishAs publishAs : values()) {
            pairs.add(new PublishAsPair(publishAs.name(), publishAs.description));
        }
        return pairs;
    }

    public static class PublishAsPair {
        private final String value;
        private final String name;

        public PublishAsPair(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "PublishAsPair{value='" + value + "', name='" + name + "'}";
        }
    }
}
