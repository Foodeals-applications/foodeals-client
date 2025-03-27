package net.foodeals.product.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
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

    public Supplement() {
    }

    public Supplement(String name, Price price,String supplementImagePath) {
        this.name = name;
        this.price = price;
        this.supplementImagePath=supplementImagePath;
    }
}