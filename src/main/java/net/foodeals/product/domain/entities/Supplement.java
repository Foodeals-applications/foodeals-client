package net.foodeals.product.domain.entities;

import java.util.UUID;

import jakarta.persistence.*;
import net.foodeals.product.domain.enums.SupplementCategory;
import org.hibernate.annotations.UuidGenerator;

import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.Deal;

@Entity
@Table(name = "supplements")
@Getter
@Setter
public class Supplement extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    @Embedded
    private Price price;
    
    @Column(name = "supplement_image_type")
    private String supplementImagePath;

   
    @ManyToOne(fetch = FetchType.LAZY)
    private Deal deal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Box box;

    @Enumerated(EnumType.STRING)
    private SupplementCategory supplementCategory ;

    public Supplement() {
    }

    public Supplement(String name, Price price,String supplementImagePath) {
        this.name = name;
        this.price = price;
        this.supplementImagePath=supplementImagePath;
    }
}