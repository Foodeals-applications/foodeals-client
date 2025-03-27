package net.foodeals.organizationEntity.domain.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "solutions")

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Solution extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "solutions")
    private Set<OrganizationEntity> organizationEntities = new HashSet<>();
    
    
    @ManyToMany(mappedBy = "solutions")  
    private List<SubEntity> subEntities;

}
