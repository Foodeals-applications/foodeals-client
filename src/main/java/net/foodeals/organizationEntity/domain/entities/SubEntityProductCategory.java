package net.foodeals.organizationEntity.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "sub_entities_products-categories")

@Getter
@Setter
@NoArgsConstructor
public class SubEntityProductCategory extends AbstractEntity<UUID> {


    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private SubEntityDomain subEntityDomain;

    public SubEntityProductCategory(String name,SubEntityDomain subEntityDomain) {
        this.subEntityDomain=subEntityDomain;
        this.name = name;
    }

    public static SubEntityProductCategory create( String name,SubEntityDomain subEntityDomain) {
        return new SubEntityProductCategory(name,subEntityDomain);
    }

}
