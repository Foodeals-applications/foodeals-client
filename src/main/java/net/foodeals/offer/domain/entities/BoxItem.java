package net.foodeals.offer.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.product.domain.entities.Product;

@Entity
@Table(name = "box_items")

@Getter
@Setter
public class BoxItem extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Embedded
    private Price price;

    private Integer quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    private Box box;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Product product;

    BoxItem() {
    }

    public BoxItem(Price price, Integer quantity, Box box, Product product) {
        this.price = price;
        this.quantity = quantity;
        this.box = box;
        this.product = product;
    }

    public static BoxItem create(Price price, Integer quantity, Box box, Product product) {
        return new BoxItem(
                price,
                quantity,
                box,
                product
        );
    }
    
    public BoxItem(Price price, Integer quantity, Product product) {
        this.price = price;
        this.quantity = quantity;
        this.product = product;
    }

    public static BoxItem create(Price price, Integer quantity, Product product) {
        return new BoxItem(
                price,
                quantity,
                product
        );
    }
    
    
}
