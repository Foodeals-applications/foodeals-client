package net.foodeals.organizationEntity.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.product.domain.entities.ProductCategory;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "sub_entities_domains")

@Getter
@Setter
@NoArgsConstructor
public class SubEntityDomain extends AbstractEntity<UUID> {


    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "subEntityDomains", cascade = CascadeType.ALL)
    private List<SubEntity> subEntities;

    @OneToMany(mappedBy = "subEntityDomain", cascade = CascadeType.ALL)
    private List<SubEntityProductCategory>subEntityProductCategories;

    public SubEntityDomain(String name) {
        this.name = name;
    }

    public static SubEntityDomain create(UUID id, String name) {
        return new SubEntityDomain(name);
    }

}
