package net.foodeals.user.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.delivery.domain.entities.CoveredZones;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.user.domain.enums.Civility;
import net.foodeals.user.domain.enums.Nationality;
import net.foodeals.user.domain.enums.UserStatus;
import net.foodeals.user.domain.valueObjects.Name;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

import java.time.LocalDate;

/**
 * User
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "users")

@Getter
public class User extends AbstractEntity<Integer> implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;

    @Embedded
    private Name name;

    private String avatarPath;

    @Column(unique = true)
    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Civility civility;

    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    private String password;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Role role;

    @ManyToOne
    private OrganizationEntity organizationEntity;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL)
    private SubEntity subEntity;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Solution> solutions;

    private String cin;

    private String rayon;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserActivities> userActivities;

    @OneToMany(mappedBy = "deliveryBoy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Delivery> deliveries;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders = new ArrayList();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // foreign key dans Address
    private List<Address> otherAddresses = new ArrayList<>();

    @OneToMany(mappedBy = "collaborator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkSchedule> workSchedules = new ArrayList();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CoveredZones> coveredZones = new ArrayList();
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;


    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String reason;

    private String motif;

    @Embedded
    private Coordinates coordinates;

    private int raduis;

    Integer distanceOfDeliveryBoy;


    @ManyToMany
    @JoinTable(
            name = "client_favoris",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id")
    )
    private List<Offer> favorisOffers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "subentites_favoris",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "subentity_id")
    )
    private List<SubEntity> favorisSubEntities = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "products_favoris",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "subentity_id")
    )
    private List<Product> favorisProducts = new ArrayList<>();


    public User(Name name, String email, String phone, String password, Boolean isEmailVerified, Role role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isEmailVerified = isEmailVerified;
        this.role = role;
    }

    public User(Name name, String avatarPath, String email, String phone, Civility civility, Nationality nationality,
                SubEntity subEntity, Role role) {
        this.name = name;
        this.avatarPath = avatarPath;
        this.email = email;
        this.phone = phone;
        this.civility = civility;
        this.nationality = nationality;
        this.subEntity = subEntity;
        this.role = role;
    }

    public User() {

    }

    public static User create(Name name, String email, String phone, String password, Boolean isEmailVerified,
                              Role role) {
        return new User(name, email, phone, password, isEmailVerified, role);
    }

    public static User create(Name name, String avatarPath, String email, String phone, Civility civility,
                              Nationality nationality, SubEntity subEntity, Role role) {
        return new User(name, avatarPath, email, phone, civility, nationality, subEntity, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null || role.getAuthorities() == null) {
            return Collections.emptyList();
        }

        final List<SimpleGrantedAuthority> authorities = role.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setName(Name name) {
        this.name = name;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public User setIsEmailVerified(Boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
        return this;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public User setAccount(Account account) {
        this.account = account;
        return this;
    }

    public User setStatus(UserStatus st) {
        this.status = st;
        return this;
    }

    public User setWorkSchedules(List<WorkSchedule> workSchedules) {
        this.workSchedules = workSchedules;
        return this;
    }

    public User setSubEntity(SubEntity subEntity) {
        this.subEntity = subEntity;
        return this;
    }

    public User setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
        return this;
    }

    public User setOrganizationEntity(OrganizationEntity organizationEntity) {
        this.organizationEntity = organizationEntity;
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public void setCivility(Civility civility) {
        this.civility = civility;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public void setUserActivities(List<UserActivities> userActivities) {
        this.userActivities = userActivities;
    }

    public void setCoveredZones(List<CoveredZones> coveredZones) {
        this.coveredZones = coveredZones;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }


    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public void setRayon(String rayon) {
        this.rayon = rayon;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }


    public void setRaduis(int raduis) {
        this.raduis = raduis;
    }

    public void setFavoris(List<Offer> favorisOffers) {
        this.favorisOffers = favorisOffers;
    }

    

    public Integer calculDistanceOfDeliveryBoy(User deliveryBoy, Coordinates positionUserConnected) {
        if (positionUserConnected.latitude() == null || positionUserConnected.longitude() == null ||
                deliveryBoy.getCoordinates().latitude() == null || deliveryBoy.getCoordinates().longitude() == null) {
            return null;
        }

        final int R = 6371;

        double latDistance = toRadians(deliveryBoy.getCoordinates().latitude() - positionUserConnected.latitude());
        double lonDistance = toRadians(deliveryBoy.getCoordinates().longitude() - positionUserConnected.longitude());

        double a = sin(latDistance / 2) * sin(latDistance / 2)
                + cos(toRadians(positionUserConnected.latitude())) * cos(toRadians(deliveryBoy.getCoordinates().latitude()))
                * sin(lonDistance / 2) * sin(lonDistance / 2);

        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return (int) (R * c); // Retourne la distance arrondie en km
    }

    public void setDistanceOfDeliveryBoy(Integer distanceOfDeliveryBoy) {
        this.distanceOfDeliveryBoy = distanceOfDeliveryBoy;
    }

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setFavorisOffers(List<Offer> favorisOffers) {
		this.favorisOffers = favorisOffers;
	}

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public void setFavorisProducts(List<Product> favorisProducts) {
        this.favorisProducts = favorisProducts;
    }

    public void setFavorisSubEntities(List<SubEntity> favorisSubEntities) {
        this.favorisSubEntities = favorisSubEntities;
    }
}
