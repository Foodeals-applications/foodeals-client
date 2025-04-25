package net.foodeals.user.infrastructure.interfaces.web;


import lombok.RequiredArgsConstructor;
import net.foodeals.location.application.dtos.requests.CreateAddressAccountRequest;
import net.foodeals.location.application.dtos.responses.AddressAccountResponse;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.location.application.services.AddressService;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AddressService addressService;
    private final UserService userService;


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

    /*public ResponseEntity<List<FavorisResponse>>getListFavorisList(){

    }*/
}
