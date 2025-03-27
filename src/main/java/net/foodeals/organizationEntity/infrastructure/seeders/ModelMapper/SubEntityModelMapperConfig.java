package net.foodeals.organizationEntity.infrastructure.seeders.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.location.application.dtos.responses.AddressResponse;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.organizationEntity.application.dtos.responses.ActivityResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SolutionResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityResponse;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.user.application.dtos.responses.RoleResponse;
import net.foodeals.user.application.dtos.responses.UserResponse;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@Configuration
@RequiredArgsConstructor
@Transactional
public class SubEntityModelMapperConfig {

	private final ModelMapper mapper;
	private final UserService userService;

	@PostConstruct
	public void configure() {
		mapper.addConverter(context -> {
			User manager = context.getSource();
			final RoleResponse roleResponse = mapper.map(manager.getRole(), RoleResponse.class);
			List<SolutionResponse> solutions = manager.getSolutions().stream()
					.map(sol -> mapper.map(sol, SolutionResponse.class)).toList();
			AddressResponse addressResponse =null;
			if(manager.getAddress()!=null) {
				addressResponse = mapper.map(manager.getAddress(), AddressResponse.class);
			}
			
			Boolean deliveryBOYAvailable=true;
            if(manager.getDeliveries()!=null) {
            	deliveryBOYAvailable=false;
            }

			return new UserResponse(manager.getId(), manager.getAvatarPath(), manager.getCivility(), manager.getName(),
					manager.getNationality(), manager.getCin(), roleResponse, manager.getEmail(),
					manager.getPhone(),null,null,null,solutions,addressResponse,null,manager.getStatus(),
					deliveryBOYAvailable,0,manager.getCoordinates())

			;
		}, User.class, UserResponse.class);

		mapper.addConverter(context -> {
			Solution solution = context.getSource();

			return new SolutionResponse(solution.getId(), solution.getName())

			;
		}, Solution.class, SolutionResponse.class);

		mapper.addConverter(context -> {
			Address address = context.getSource();
			final CityResponse cityResponse = mapper.map(address.getCity(), CityResponse.class);
			return new AddressResponse(address.getId(), address.getAddress(), address.getExtraAddress(),
					address.getZip(), cityResponse)

			;
		}, Address.class, AddressResponse.class);

		mapper.addConverter(context -> {
			SubEntity subEntity = context.getSource();
			Address address = subEntity.getAddress();
			AddressResponse addressResponse = mapper.map(address, AddressResponse.class);
			int numberOfCollabs = subEntity.getUsers().size();
			Date creationDate = subEntity.getCreatedAt() != null ? Date.from(subEntity.getCreatedAt()) : null;

			Integer managerId = subEntity.getManager() != null ? subEntity.getManager().getId() : null;
			UserResponse manager = mapper.map(userService.findById(managerId), UserResponse.class);
			List<ActivityResponse> activityReponses = subEntity.getActivities().stream()
					.map(activity -> mapper.map(activity, ActivityResponse.class)).toList();
			int numberOfOffers = subEntity.getActivities().stream().flatMap(activity -> activity.getOffers().stream())
					.collect(Collectors.toList()).size();

			int numberOfOrders = subEntity.getActivities().stream().flatMap(activity -> activity.getOffers().stream())
					.flatMap(offer -> offer.getOrders().stream()).collect(Collectors.toList()).size();

			List<SolutionResponse> solutions = subEntity.getSolutions().stream()
					.map(solution -> mapper.map(solution, SolutionResponse.class)).toList();
			return new SubEntityResponse(subEntity.getId(), subEntity.getName(), activityReponses,
					subEntity.getAvatarPath(), subEntity.getCoverPath(), subEntity.getEmail(), subEntity.getPhone(),
					solutions, addressResponse, subEntity.getIFrame(), creationDate, numberOfCollabs, numberOfOffers,
					numberOfOrders, manager,subEntity.getSubEntityStatus());

		}, SubEntity.class, SubEntityResponse.class);

	}
}
