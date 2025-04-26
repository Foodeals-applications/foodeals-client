package net.foodeals.user.infrastructure.interfaces.web;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.foodeals.location.application.dtos.requests.CreateAddressAccountRequest;
import net.foodeals.location.application.dtos.responses.AddressAccountResponse;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.location.application.services.AddressService;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.user.application.dtos.responses.FavorisOfferPartenerResponse;
import net.foodeals.user.application.dtos.responses.FavorisOfferResponse;
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
    private final DealRepository dealRepository;
    private final UserRepository userRepository;


    @PostMapping("/address/create")
    public ResponseEntity<AddressAccountResponse> createAddress(@RequestBody CreateAddressAccountRequest request) {
        Address savedAddress = addressService.createAccountAddress(request);
        User connectedUser = userService.getConnectedUser();

        AddressAccountResponse response = new AddressAccountResponse(savedAddress.getAddressType(),savedAddress.getContactName(),
                savedAddress.getContactEmail(),savedAddress.getContactPhone(),savedAddress.getAddress(), savedAddress.getExtraAddress(),
                savedAddress.getZip(),connectedUser.getCoordinates().latitude(),connectedUser.getCoordinates().longitude(),
                savedAddress.getCity().getName(),savedAddress.getRegion().getName(),savedAddress.getCountry().getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/address/delete/{id}")
    public ResponseEntity<String> createAddress(@PathVariable UUID id) {
        addressService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("L'adresse est bien supprimé");
    }


    @GetMapping("/favoris/add-to-favoris/{idDeal}")
    public ResponseEntity<String>addOfferToDeal(@PathVariable UUID idDeal){

        Deal deal=dealRepository.findById(idDeal).orElse(null);
        if(deal==null){return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
        User user=userService.getConnectedUser();
        List<Offer>favorisOffers=user.getFavorisOffers();
        favorisOffers.add(deal.getOffer());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Favoris bien ajouté");
    }

    @GetMapping("/favoris/offers")
    public ResponseEntity<List<FavorisOfferResponse>>getListFavorisOffersList(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getListFavorisOffers());
    }

    @GetMapping("/favoris/partners-offers")
    public ResponseEntity<List<FavorisOfferPartenerResponse>>getListFavorisOffersPartnersList(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getListFavorisOffersPartners());
    }
}
