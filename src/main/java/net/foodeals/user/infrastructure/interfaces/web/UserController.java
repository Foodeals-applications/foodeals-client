package net.foodeals.user.infrastructure.interfaces.web;

import java.util.List;
import java.util.Map;

import net.foodeals.core.domain.entities.Coordinates;
import net.foodeals.core.domain.entities.User;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.user.application.dtos.responses.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.services.OfferService;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.user.application.dtos.requests.PostionClientRequest;
import net.foodeals.user.application.services.UserService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;
	private final OfferService offerService;
	private final SubEntityService subEntityService;

	@PostMapping("/set-position-client")
	public ResponseEntity<PositionClientResponse> setPositionOfClient(@RequestBody PostionClientRequest request) {
		final User user = service.setPositionClient(request.id(), request.coordinates(), request.raduis());
		final PositionClientResponse positionClientResponse = new PositionClientResponse(user.getId(),
				user.getCoordinates(), user.getRaduis());
		return ResponseEntity.ok(positionClientResponse);
	}

    @GetMapping("/nearby")
    public ResponseEntity<Map<String, Object>> getNears(
            @RequestParam Float latitude,
            @RequestParam Float longitude,
            @RequestParam Float radius) {

        User user = service.getConnectedUser();

        Float lat = latitude;
        Float lng = longitude;

        if (user.getCoordinates() != null) {
            if (user.getCoordinates().latitude() != null &&
                    user.getCoordinates().longitude() != null) {

                lat = user.getCoordinates().latitude();
                lng = user.getCoordinates().longitude();
            }
        }

        Map<String, Object> result =
                offerService.getNears(lat, lng, radius);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/me/statistics")
    public UserStatisticsResponse getMyStatistics() {
        return service.getStatistics();
    }

	@GetMapping("/store-count")
	public ResponseEntity<List<Map<String, Object>>> getStoreCountByDomains() {
		return ResponseEntity.ok(subEntityService.getStoreCountByDomains());
	}



    @PostMapping("/upload")
    public ResponseEntity<AvatarUploadResponse> uploadAvatar(
            @RequestParam("file") MultipartFile file
    ) {
        User user=service.getConnectedUser();
        try {
            String avatarPath = service.uploadAvatar(user.getId(), file);
            return ResponseEntity.ok(new AvatarUploadResponse(avatarPath));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/favorites")
    public FavoritesResponse getUserFavorites() {
        // ⚠️ Récupération de l'utilisateur connecté par email
        User user = service.getConnectedUser();

        // Mapping SubEntities -> PartnerResponse
        List<PartnerResponse> partners = user.getFavorisSubEntities()
                .stream()
                .map(PartnerResponse::fromEntity)
                .toList();

        // Mapping Products -> ProductResponse
        List<ProductResponse> products = user.getFavorisProducts()
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();

        return new FavoritesResponse(partners, products);
    }

	/*
	 * @PostMapping public ResponseEntity<PositionClientResponse>
	 * searchListOfPartenrsByPosition(PostionClientRequest request) { final User
	 * user =service.setPositionClient(request.id(),request.coordinates(),
	 * request.raduis()); final PositionClientResponse positionClientResponse=new
	 * PositionClientResponse(user.getId(), user.getCoordinates(),
	 * user.getRaduis()); return ResponseEntity.ok(positionClientResponse); }
	 */

}
