package net.foodeals.user.infrastructure.interfaces.web;

import java.util.List;
import java.util.Map;

import net.foodeals.user.application.dtos.responses.AvatarUploadResponse;
import net.foodeals.user.application.dtos.responses.UserStatisticsResponse;
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
import net.foodeals.user.application.dtos.responses.PositionClientResponse;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
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
	public ResponseEntity<Map<String, Object>> getNears(@RequestParam double latitude, @RequestParam double longitude,
			@RequestParam double radius) {
        User user=service.getConnectedUser();
		Map<String, Object> result = offerService.getNears(user.getCoordinates().latitude(),
                user.getCoordinates().longitude(), radius);
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
	

	/*
	 * @PostMapping public ResponseEntity<PositionClientResponse>
	 * searchListOfPartenrsByPosition(PostionClientRequest request) { final User
	 * user =service.setPositionClient(request.id(),request.coordinates(),
	 * request.raduis()); final PositionClientResponse positionClientResponse=new
	 * PositionClientResponse(user.getId(), user.getCoordinates(),
	 * user.getRaduis()); return ResponseEntity.ok(positionClientResponse); }
	 */

}
