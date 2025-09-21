package net.foodeals.offer.domain.enums;

import java.util.ArrayList;
import java.util.List;

public enum Category {
	// Categories for Bakeries / Pastries
	BREAD_AND_PASTRIES(PublishAs.BAKERIES_PASTRIES, "Pain et pâtisseries"),
	PASTRIES(PublishAs.BAKERIES_PASTRIES, "Pâtisseries"), CAKES(PublishAs.BAKERIES_PASTRIES, "Gâteaux"),
	SMALL_PASTRIES(PublishAs.BAKERIES_PASTRIES, "Petites pâtisseries"),
	SAVORY_PRODUCTS(PublishAs.BAKERIES_PASTRIES, "Produits salés"),

	// Categories for Supermarkets / Hypermarkets
	FRUITS_AND_VEGETABLES(PublishAs.SUPERMARKETS_HYPERMARKETS, "Fruits et légumes"),
	MEAT_AND_FISH(PublishAs.SUPERMARKETS_HYPERMARKETS, "Viandes et poissons"),
	BISCUITS_AND_CONFECTIONERY(PublishAs.SUPERMARKETS_HYPERMARKETS, "Biscuits et confiseries"),
	DAIRY_PRODUCTS(PublishAs.SUPERMARKETS_HYPERMARKETS, "Produits laitiers"),
	CEREAL_PRODUCTS(PublishAs.SUPERMARKETS_HYPERMARKETS, "Produits céréaliers"),
	CANNED_PRODUCTS(PublishAs.SUPERMARKETS_HYPERMARKETS, "Produits en conserve"),
	FROZEN_PRODUCTS(PublishAs.SUPERMARKETS_HYPERMARKETS, "Produits surgelés"),
	GROCERY_PRODUCTS(PublishAs.SUPERMARKETS_HYPERMARKETS, "Produits d'épicerie"),
	WATER_AND_BEVERAGES(PublishAs.SUPERMARKETS_HYPERMARKETS, "Eaux et boissons"),

	// Categories for Restaurants / Hotels / Caterers
	FAST_FOOD(PublishAs.RESTAURANTS_HOTELS_CATERERS, "Restauration rapide"),
	DISHES(PublishAs.RESTAURANTS_HOTELS_CATERERS, "Plats"), ASIAN(PublishAs.RESTAURANTS_HOTELS_CATERERS, "Asiatique"),
	SALADS(PublishAs.RESTAURANTS_HOTELS_CATERERS, "Salades"),
	DESSERTS(PublishAs.RESTAURANTS_HOTELS_CATERERS, "Desserts"),

	// Categories for Wholesalers and Industrial
	WHOLESALER_FRUITS_AND_VEGETABLES(PublishAs.WHOLESALERS_INDUSTRIALS, "Grossiste Fruits et Légumes"),
	WHOLESALER_BISCUITS_AND_CONFECTIONERY(PublishAs.WHOLESALERS_INDUSTRIALS, "Grossiste Biscuits et Confiseries"),
	WHOLESALER_DAIRY_PRODUCTS(PublishAs.WHOLESALERS_INDUSTRIALS, "Grossiste Produits Laitiers"),
	WHOLESALER_DRY_PRODUCTS(PublishAs.WHOLESALERS_INDUSTRIALS, "Grossiste Produits Secs"),
	WHOLESALER_CANNED_PRODUCTS(PublishAs.WHOLESALERS_INDUSTRIALS, "Grossiste Produits en Conserve"),
	WHOLESALER_FROZEN_PRODUCTS(PublishAs.WHOLESALERS_INDUSTRIALS, "Grossiste Produits Surgelés"),
	WHOLESALER_GROCERY_PRODUCTS(PublishAs.WHOLESALERS_INDUSTRIALS, "Grossiste Produits d'Épicerie"),
	WHOLESALER_BEVERAGES(PublishAs.WHOLESALERS_INDUSTRIALS, "Grossiste Boissons");

	private final PublishAs publishAs;
	private final String name;

	Category(PublishAs publishAs, String name) {
		this.publishAs = publishAs;
		this.name = name;
	}

	public PublishAs getDealType() {
		return publishAs;
	}

	public String getName() {
		return name;
	}

    public static Category fromString(String str) {
        if (str == null) return null;
        String normalized = str.trim().replace(" ", "_").toUpperCase();
        for (Category c : values()) {
            if (c.name().equals(normalized)) {
                return c;
            }
        }
        return null; // ou lever exception personnalisée
    }

	public static List<CategoryPair> getCategoryPairs() {
		List<CategoryPair> pairs = new ArrayList();
		for (Category category : values()) {
			pairs.add(new CategoryPair(category.name(), category.getName()));
		}
		return pairs;
	}

	public static class CategoryPair {
		private final String value;
		private final String name;

		public CategoryPair(String value, String name) {
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
			return "CategoryPair{value='" + value + "', name='" + name + "'}";
		}
	}

}
