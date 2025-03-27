package net.foodeals.offer.domain.entities;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.offer.domain.entities.Deal;

import java.util.List;
import java.util.UUID;

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
    @JoinColumn(name = "deal_id", nullable = false)
    private Deal deal;

    @Column(nullable = false)
    private Integer quantity;

    public CartItem() {}

    public CartItem(Cart cart, Deal deal, Integer quantity) {
        this.cart = cart;
        this.deal = deal;
        this.quantity = quantity;
    }
}

