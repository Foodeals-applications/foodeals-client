package net.foodeals.offer.domain.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.DealStatus;
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.enums.PublishAs;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.product.domain.entities.PriceBlock;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.Supplement;

@Entity
@Table(name = "deals")
@Getter
@Setter
public class Deal extends AbstractEntity<UUID> implements IOfferChoice {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    
    private String title;
    
    private String description ;
    
    private Price price;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Product product;

    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Supplement> supplements;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PublishAs publishAs;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Override
    public final OfferType getOfferType() {
        return OfferType.DEAL;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    private Offer offer;

    @Enumerated(EnumType.STRING)
    private DealStatus dealStatus;

    private String reason;

    private String motif;

    @Enumerated(EnumType.STRING)
    private DealUnityType dealUnityType;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PriceBlock> defaultPrices;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PriceBlock> customPrices;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private OrganizationEntity creator ; 
    
    private Date expirationDate;
    
    public Deal() {
    }

    
    public Deal(Price price, int quantity, Product product, List<Supplement> supplements, PublishAs publishAs,
            Category category, Offer offer) {
        this.price = price;
        this.quantity = quantity;
        this.product = product;
        this.supplements = supplements;
        this.publishAs = publishAs;
        this.category = category;
        this.offer = offer;
    }
    
    public Deal(int quantity, Product product, List<Supplement> supplements, PublishAs publishAs,
            Category category, Offer offer, DealUnityType dealUnityType, List<PriceBlock> defaultPrices,
            List<PriceBlock> customPrices) {
        this.quantity = quantity;
        this.product = product;
        this.supplements = supplements;
        this.publishAs = publishAs;
        this.category = category;
        this.offer = offer;
        this.dealUnityType = dealUnityType;
        this.defaultPrices = defaultPrices;
        this.customPrices=customPrices;
    }

    public static Deal create(Integer quantity, Product product, List<Supplement> supplements,
            PublishAs publishAs, Category category, Offer offer, DealUnityType dealUnityType, List<PriceBlock> defaultPrices,
            List<PriceBlock> customPrices) {
        return new Deal(quantity, product, supplements, publishAs, category, offer, dealUnityType, 
        		defaultPrices,customPrices);
    }
    
    public static Deal create(Price price, Integer quantity, Product product, List<Supplement> supplements,
            PublishAs publishAs, Category category, Offer offer) {
        return new Deal(price, quantity, product, supplements, publishAs, category, offer);
    }

    // Méthodes pour définir les prix personnalisés
    public void setCustomPrices(List<PriceBlock> customPrices) {
        this.customPrices = customPrices;
    }

    public void setDefaultPrices(List<PriceBlock> defaultPrices) {
        this.defaultPrices = defaultPrices;
    }

    // Autres setters pour la logique existante
    public Deal setPrice(Price price) {
        this.price = price;
        return this;
    }

    public Deal setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Deal setProduct(Product product) {
        this.product = product;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setDealStatus(DealStatus dealStatus) {
        this.dealStatus = dealStatus;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public void setSupplements(List<Supplement> supplements) {
        this.supplements = supplements;
    }

    public void setPublishAs(PublishAs publishAs) {
        this.publishAs = publishAs;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public enum DealUnityType {
        PRODUCT,
        LOT
    }
    
    

    
}
