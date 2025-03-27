package net.foodeals.delivery.interfaces.web;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.delivery.application.dtos.requests.delivery.DeliveryPositionRequest;
import net.foodeals.delivery.application.dtos.requests.delivery.DeliveryRequest;
import net.foodeals.delivery.application.dtos.responses.DeliveryManDetailsResponse;
import net.foodeals.delivery.application.dtos.responses.DeliveryManPositionsResponse;
import net.foodeals.delivery.application.dtos.responses.DeliveryTrackingResponse;
import net.foodeals.delivery.application.dtos.responses.DetailsPartenerOfOrderResponse;
import net.foodeals.delivery.application.services.DeliveryService;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.delivery.domain.entities.DeliveryPosition;
import net.foodeals.delivery.domain.enums.DeliveryStatus;
import net.foodeals.delivery.domain.repositories.DeliveryRepository;
import net.foodeals.location.application.dtos.responses.AddressResponse;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.order.application.dtos.responses.DeliveryResponse;
import net.foodeals.order.application.dtos.responses.OrderDetailsResponse;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.organizationEntity.application.dtos.responses.OrganizationEntityResponse;
import net.foodeals.organizationEntity.application.services.OrganizationEntityService;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.user.application.dtos.responses.UserResponse;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;

@RestController
@RequestMapping("v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService service;
	private final UserService userService ;
	private final DeliveryRepository repository;
	private final UserRepository userRepository;
	private final OrderService orderService;
	private final DeliveryRepository deliveryRepository;
	private final OrganizationEntityService organizationEntityService;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<List<Delivery>> findAll() {
		final List<Delivery> deliveries = service.findAll();
		return ResponseEntity.ok(deliveries);
	}

	@GetMapping("/byPartener")
	public ResponseEntity<List<UserResponse>> findAllByPartener(@RequestParam Float latitude,
			@RequestParam Float longtitude) {

		Coordinates coordinates = new Coordinates(latitude, longtitude);
		List<UserResponse> responses = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String email = userDetails.getUsername();
			User user = userRepository.findByEmail(email).get();
			user.setCoordinates(coordinates);
			OrganizationEntity organizationEntity = user.getOrganizationEntity();
			List<User> users = organizationEntity.getUsers();
			for (User u : users) {
				if (u.getRole().getName().equals("DELIVERY_MAN")) {
					Integer distanceOfDeliveryBoy = u.calculDistanceOfDeliveryBoy(u, coordinates);
					u.setDistanceOfDeliveryBoy(distanceOfDeliveryBoy);
					UserResponse response = mapper.map(u, UserResponse.class);
					responses.add(response);
				}
			}
		}

		return ResponseEntity.ok(responses);
	}

	@GetMapping("/getAllDeliveryManPositions/{id}")
	public ResponseEntity<DeliveryManPositionsResponse> getAllDeliveryManPositions(@PathVariable UUID id) {

		DeliveryManPositionsResponse response = null;
		List<UserResponse> boys = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String email = userDetails.getUsername();
			User user = userRepository.findByEmail(email).get();
			OrganizationEntity organizationEntity = user.getOrganizationEntity();
			List<User> users = organizationEntity.getUsers();
			for (User u : users) {
				if (u.getRole().getName().equals("DELIVERY_MAN")) {
					UserResponse userResponse = mapper.map(u, UserResponse.class);
					boys.add(userResponse);
				}
			}
			Order order = orderService.findById(id);
			User client = order.getClient();
			response = new DeliveryManPositionsResponse(boys, organizationEntity.getAddress().getCoordinates(),
					client.getCoordinates());

		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("detailsDeliveryBoy/{idOrder}/{idBoy}")
	public ResponseEntity<DeliveryManDetailsResponse> getDetailsDeliveryMan(@PathVariable Integer idBoy,
			@PathVariable UUID idOrder){
		
		User boy= userRepository.findById(idBoy).get();
		Order order =orderService.findById(idOrder);
		Integer distanceOfDeliveryBoy = boy.calculDistanceOfDeliveryBoy(boy, boy.getCoordinates());
		DeliveryManDetailsResponse response =new DeliveryManDetailsResponse(idBoy, 
				boy.getName().lastName(), boy.getName().firstName(), distanceOfDeliveryBoy, 
				order.getShippingAddress().getAddress(),
				order.getCreatedAt() != null ? Date.from(order.getCreatedAt()) : null , order.getOffer().getModalityPaiement());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DeliveryResponse> findById(@PathVariable UUID id) {
		final DeliveryResponse delivery = mapper.map(service.findById(id), DeliveryResponse.class);
		return ResponseEntity.ok(delivery);
	}

	@PostMapping
	public ResponseEntity<DeliveryResponse> create(@RequestBody @Valid DeliveryRequest request) {
		final DeliveryResponse deliveryResponse = mapper.map(service.create(request), DeliveryResponse.class);
		return new ResponseEntity<>(deliveryResponse, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<DeliveryResponse> update(@PathVariable UUID id, @RequestBody @Valid DeliveryRequest request) {
		final DeliveryResponse delivery = mapper.map(service.update(id, request), DeliveryResponse.class);
		return ResponseEntity.ok(delivery);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/stats")
	public Map<String, Object> getDashboardStats(@RequestParam("deliveryBoyId") Integer deliveryBoyId,
			@RequestParam("startDate") Instant startDate, @RequestParam("endDate") Instant endDate) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UUID organizationId = null;
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String email = userDetails.getUsername();
			User user = userRepository.findByEmail(email).get();
			organizationId = user.getOrganizationEntity().getId();
		}

		return Map.of("totalOrders", service.getTotalOrders(deliveryBoyId, startDate, endDate), "ordersInProgress",
				service.getOrdersInProgress(deliveryBoyId, startDate, endDate), "scheduledOrders",
				service.getScheduledOrders(deliveryBoyId, startDate, endDate), "deliveredOrders",
				service.getDeliveredOrders(deliveryBoyId, startDate, endDate), "activeDeliveryBoys",
				service.getActiveDeliveryBoys(organizationId), "inactiveDeliveryBoys",
				service.getInactiveDeliveryBoys(organizationId));
	}

	/*@PostMapping("/{id}/position")
	public ResponseEntity<DeliveryTrackingResponse> updatePosition(@PathVariable UUID id,
			@RequestBody DeliveryPositionRequest positionRequest) {
		Delivery delivery = repository.findById(id).orElse(null);

		if (delivery == null) {
			return ResponseEntity.notFound().build();
		}

		DeliveryPosition position = new DeliveryPosition();
		position.setCoordinates(positionRequest.coordinates());
		delivery.setDeliveryPosition(position);

		delivery = repository.save(delivery);

		OrganizationEntity partner=userService.getConnectedUser().getOrganizationEntity();
		DeliveryTrackingResponse deliveryResponse = new DeliveryTrackingResponse(delivery.getDeliveryBoy().getId(),
				delivery.getOrder().getId(), partner.getId(), 
				delivery.getOrder().getClient().getId(), delivery.getStatus(),delivery.getDeliveryBoy().getCoordinates());
	
		return ResponseEntity.ok(deliveryResponse); 
	}
    */
	@PutMapping("/{id}/{status}")
	public ResponseEntity<?> updateStatus(@PathVariable UUID id, @RequestParam DeliveryStatus status) {
		Delivery delivery = service.findById(id);
		if (delivery == null) {
			return ResponseEntity.notFound().build();
		}

		delivery.setStatus(status);
		delivery = repository.save(delivery);
		final DeliveryResponse deliveryResponse = mapper.map(delivery, DeliveryResponse.class);
		return ResponseEntity.ok(deliveryResponse);
	}

	@GetMapping("/{id}/confirm")
	public ResponseEntity<?> confirmDelivery(@PathVariable UUID id) {
		Delivery delivery = service.findById(id);
		if (delivery == null) {
			return ResponseEntity.notFound().build();
		}

		delivery.setStatus(DeliveryStatus.DELIVERED);
		delivery = repository.save(delivery);
		final DeliveryResponse deliveryResponse = mapper.map(delivery, DeliveryResponse.class);
		return ResponseEntity.ok(deliveryResponse);
	}
	
	
	@GetMapping("/scan")
    public ResponseEntity<OrganizationEntityResponse> scanQRCode(
            @RequestParam String qrCode,
            @RequestParam UUID orderId,
            @RequestParam UUID deliveryMenId) {

        Order order = orderService.findById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        // Vérification du QR Code (à adapter selon la logique métier)
        if (!qrCode.equals(orderId.toString())) {
            return ResponseEntity.badRequest().build();
        }

        // Récupération de l'organisation associée à la commande
        OrganizationEntity organization = userService.getConnectedUser().getOrganizationEntity();
        if (organization == null) {
            return ResponseEntity.notFound().build();
        }

        // Construction de la réponse DTO
        OrganizationEntityResponse response = new OrganizationEntityResponse
        		(organization.getId(), organization.getName(), organization.getAvatarPath(), 
        				organization.getCoverPath(), organization.getType(), 
        				new AddressResponse(organization.getAddress().getId(), 
        						organization.getAddress().getAddress(), organization.getAddress().getExtraAddress(), 
        						organization.getAddress().getZip(),new CityResponse(organization.getAddress().getCity().getId(),
        								organization.getAddress().getCity().getName(), 
        								organization.getAddress().getCity().getCode(), null, null,organization.getAddress().getCity().getCoordinates())), qrCode)
        ;

        return ResponseEntity.ok(response);
    }
	
	
	@GetMapping("/showDetailsPartnerOfOrder/{id}")
	public ResponseEntity<DetailsPartenerOfOrderResponse> showDetailsPartnerOfOrder(@PathVariable UUID id) {
		Order order =orderService.findById(id);
		if (order == null) {
			return ResponseEntity.notFound().build();
		}

		OrganizationEntity organizationEntity=userService.getConnectedUser().getOrganizationEntity();
		String addressDelivery=order.getShippingAddress().getAddress()+" "+order.getShippingAddress().getCity().getName();
		ModalityPaiement modalityPaiement=null;
		if(order.getOffer().getModalityPaiement()==null) {
			modalityPaiement=ModalityPaiement.CARD;
		}
		else {
			modalityPaiement=order.getOffer().getModalityPaiement();
		}
		DetailsPartenerOfOrderResponse response =new DetailsPartenerOfOrderResponse(order.getId(),
				organizationEntity.getName(),organizationEntity.getAvatarPath(),addressDelivery,
				order.getCreatedAt() != null ? Date.from(order.getCreatedAt()) : null,modalityPaiement.name());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/showDetailsClientOfOrder/{id}")
	public ResponseEntity<DetailsPartenerOfOrderResponse> showDetailsClientOfOrder(@PathVariable UUID id) {
		Order order =orderService.findById(id);
		if (order == null) {
			return ResponseEntity.notFound().build();
		}

		ModalityPaiement modalityPaiement=null;
		if(order.getOffer().getModalityPaiement()==null) {
			modalityPaiement=ModalityPaiement.CARD;
		}
		else {
			modalityPaiement=order.getOffer().getModalityPaiement();
		}
		
		String addressDelivery=order.getShippingAddress().getAddress()+" "+order.getShippingAddress().getCity().getName();
		DetailsPartenerOfOrderResponse response =new DetailsPartenerOfOrderResponse(order.getId(),
				order.getClient().getName().firstName(),order.getClient().getName().lastName(),addressDelivery,
				order.getCreatedAt() != null ? Date.from(order.getCreatedAt()) : null,modalityPaiement.name());
		return ResponseEntity.ok(response);
	}
	
	
	
}
