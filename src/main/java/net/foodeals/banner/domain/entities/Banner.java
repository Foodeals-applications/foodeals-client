package net.foodeals.banner.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import org.apache.commons.math3.analysis.function.Abs;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Banner extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String imageUrl; // lien vers l’image (hébergée en local ou cloud)
    private String link;     // lien cliquable

    public Banner(String link, String imageUrl) {
        this.link = link;
        this.imageUrl = imageUrl;
    }
}
