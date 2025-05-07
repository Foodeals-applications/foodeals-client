package net.foodeals.offer.domain.entities;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.offer.domain.entities.Deal;

import java.util.List;
import java.util.UUID;

import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.product.domain.entities.Product;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItem extends AbstractEntity<UUID>{
    
	
	@Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "deal_id", nullable = true)
    private Deal deal;

    @ManyToOne
    @JoinColumn(name = "box_id", nullable = true)
    private Box box;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    private ModalityType modalityType;

    public CartItem() {}

    public CartItem(Cart cart, Deal deal, Integer quantity,ModalityType modalityType) {
        this.cart = cart;
        this.deal = deal;
        this.quantity = quantity;
        this.modalityType = modalityType;
    }

    public CartItem(Cart cart, Box box, Integer quantity,ModalityType modalityType) {
        this.cart = cart;
        this.box = box;
        this.quantity = quantity;
        this.modalityType = modalityType;
    }


    public CartItem(Cart cart, Product product, Integer quantity,ModalityType modalityType) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.modalityType = modalityType;
    }
}

