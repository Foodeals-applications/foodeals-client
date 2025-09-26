package net.foodeals.user.infrastructure.interfaces.web;


import lombok.RequiredArgsConstructor;
import net.foodeals.location.application.dtos.requests.CreateAddressAccountRequest;
import net.foodeals.location.application.dtos.responses.AddressAccountResponse;
import net.foodeals.location.application.services.AddressService;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.offer.application.dtos.requests.DonationRequest;
import net.foodeals.offer.application.dtos.responses.DonationListResponse;
import net.foodeals.offer.application.dtos.responses.DonationResponse;
import net.foodeals.offer.application.services.DonationService;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.order.application.dtos.requests.AddCouponRequest;
import net.foodeals.order.application.dtos.responses.ActivateCouponResponse;
import net.foodeals.order.application.dtos.responses.AddCouponResponse;
import net.foodeals.order.application.dtos.responses.CouponsResponse;
import net.foodeals.order.application.services.CouponService;
import net.foodeals.user.application.dtos.requests.InfosProfileRequest;
import net.foodeals.user.application.dtos.requests.RatingUpdateRequest;
import net.foodeals.user.application.dtos.responses.FavorisOfferPartenerResponse;
import net.foodeals.user.application.dtos.responses.FavorisOfferResponse;
import net.foodeals.user.application.dtos.responses.InfosProfileResponse;
import net.foodeals.user.application.dtos.responses.RatingResponse;
import net.foodeals.user.application.services.RatingService;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AddressService addressService;
    private final UserService userService;
    private final CouponService couponService;
    private final DealRepository dealRepository;
    private final UserRepository userRepository;
    private final DonationService donationService;
    private final RatingService ratingService;


    @PostMapping("/address/create")
    public ResponseEntity<AddressAccountResponse> createAddress(@RequestBody CreateAddressAccountRequest request) {
        Address savedAddress = addressService.createAccountAddress(request);
        User connectedUser = userService.getConnectedUser();

        AddressAccountResponse response = new AddressAccountResponse(savedAddress.getAddressType(), savedAddress.getContactName(),
                savedAddress.getContactEmail(), savedAddress.getContactPhone(), savedAddress.getAddress(), savedAddress.getExtraAddress(),
                savedAddress.getZip(), connectedUser.getCoordinates().latitude(), connectedUser.getCoordinates().longitude(),
                savedAddress.getCity().getName(), savedAddress.getRegion().getName(), savedAddress.getCountry().getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/address/delete/{id}")
    public ResponseEntity<String> createAddress(@PathVariable UUID id) {
        addressService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("L'adresse est bien supprimé");
    }


    @GetMapping("/favoris/add-to-favoris/{idDeal}")
    public ResponseEntity<String> addOfferToDeal(@PathVariable UUID idDeal) {

        Deal deal = dealRepository.findById(idDeal).orElse(null);
        if (deal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User user = userService.getConnectedUser();
        List<Offer> favorisOffers = user.getFavorisOffers();
        favorisOffers.add(deal.getOffer());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Favoris bien ajouté");
    }

    @GetMapping("/favoris/offers")
    public ResponseEntity<List<FavorisOfferResponse>> getListFavorisOffersList() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getListFavorisOffers());
    }

    @GetMapping("/favoris/partners-offers")
    public ResponseEntity<List<FavorisOfferPartenerResponse>> getListFavorisOffersPartnersList() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getListFavorisOffersPartners());
    }

    @GetMapping("/infos-profile")
    public ResponseEntity<InfosProfileResponse> getInfosProfile() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getInfosProfile());
    }

    @PutMapping("/infos-profile/update")
    public ResponseEntity<InfosProfileResponse> getInfosProfile(@RequestBody InfosProfileRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateInfosProfile(request));
    }

    @GetMapping("/coupons")
    public CouponsResponse getCoupons() {
        User user = userService.getConnectedUser();
        return couponService.getUserCoupons(user);
    }

    @PostMapping("/coupons/add")
    public AddCouponResponse addCoupon(@RequestBody AddCouponRequest request) {
        User user = userService.getConnectedUser();
        return couponService.addCoupon(user, request.getCode());
    }

    @PostMapping("/coupons/{id}/activate")
    public ActivateCouponResponse activateCoupon(@PathVariable UUID id) {
        return couponService.activateCoupon(id);
    }


    // GET /v1/account/donations
    @GetMapping("/donations")
    public DonationListResponse getUserDonations() {
        User user = userService.getConnectedUser();
        return donationService.getUserDonations(user);
    }

    // POST /v1/donations/create
    @PostMapping("/donations/create")
    public DonationResponse createDonation(
            @RequestBody DonationRequest request) {
        User user = userService.getConnectedUser();
        return donationService.createDonation(user, request);
    }

    @GetMapping("/ratings")
    public List<RatingResponse> getUserRatings() {
        User user = userService.getConnectedUser();
        return ratingService.getUserRatings(user);
    }

    @PutMapping("/ratings/{id}")
    public RatingResponse updateRating(
            @PathVariable UUID id,
            @RequestBody RatingUpdateRequest request
    ) {
        User user = userService.getConnectedUser();
        return ratingService.updateRating(id, request, user);
    }
}
