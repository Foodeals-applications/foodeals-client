package net.foodeals.offer.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.offer.domain.entities.CartItem;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart extends AbstractEntity<UUID>{
    
	@Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private Integer userId;

    private String timeSlot;

    private String subEntityAddress;

    private boolean isDonation;

    private boolean showInfoDonation;

    @Column(name = "client_address")
    private String clientAddress;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> items=new ArrayList<>();

    public Cart() {}

    public Cart(Integer userId) {
        this.userId = userId;
    }
    
}
