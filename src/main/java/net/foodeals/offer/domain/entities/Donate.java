package net.foodeals.offer.domain.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.foodeals.offer.domain.enums.*;
import net.foodeals.user.domain.entities.User;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;

@Entity
@Table(name = "donates")
@Getter
@Setter
public class Donate extends AbstractEntity<UUID>{

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    private DonateItems donateItems ;


    @ManyToOne(cascade = CascadeType.ALL)
    private OrganizationEntity donor;

    @ManyToOne(cascade = CascadeType.ALL)
    private User userDonor ;

    @ManyToOne(cascade = CascadeType.ALL)
    private OrganizationEntity receiver ;

    private ModalityType modalityType;

    private ModalityPaiement modalityPaiement;

    @Enumerated(EnumType.STRING)
    private DonateStatus donateStatus ;

    @Enumerated(EnumType.STRING)
    private DonationType donationType ;

    @Enumerated(EnumType.STRING)
    private DonateUnity donationUnity ;

    @Enumerated(EnumType.STRING)
    private DonateDeliveryMethod donateDelivryMethod ;

    private Long deliveryFee;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<OpenTime> openTime;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "donate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    private String motif;

    private String reason ;

    private String attechementFilePath ;

    private Boolean isAnonymous;



}
