package net.foodeals.user.infrastructure.modelMapperConfig;

import java.util.List;

import net.foodeals.core.domain.entities.Authority;
import net.foodeals.core.domain.entities.Name;
import net.foodeals.core.domain.entities.Role;
import net.foodeals.core.domain.entities.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.context.DelegatingApplicationListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.foodeals.location.application.dtos.responses.AddressResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SolutionResponse;
import net.foodeals.user.application.dtos.responses.AuthorityResponse;
import net.foodeals.user.application.dtos.responses.ClientDto;
import net.foodeals.user.application.dtos.responses.RoleResponse;
import net.foodeals.user.application.dtos.responses.UserResponse;
import net.foodeals.user.application.dtos.responses.WorkScheduleResponse;
import net.foodeals.user.application.services.UserService;

@Configuration
@RequiredArgsConstructor
public class UserModelMapperConfig {

	private final ModelMapper modelMapper;
	private final DelegatingApplicationListener delegatingApplicationListener;
	private final UserService service;

	@PostConstruct
	public void configureModelMapper() {
		modelMapper.addConverter(context -> {
			final Authority authority = context.getSource();
			return new AuthorityResponse(authority.getId(), authority.getName(), authority.getValue());
		}, Authority.class, AuthorityResponse.class);

		modelMapper.addConverter(context -> {
			final Role role = context.getSource();
			final var authorities = role.getAuthorities().stream()
					.map(authority -> modelMapper.map(authority, AuthorityResponse.class)).toList();
			return new RoleResponse(role.getId(), role.getName(), authorities);
		}, Role.class, RoleResponse.class);

		modelMapper.addConverter(context -> {
			final User user = context.getSource();
			final RoleResponse roleResponse = modelMapper.map(user.getRole(), RoleResponse.class);
			System.out.println("user converter is working");
			System.out.println(user.getRole());

			Name managerName = null;
			if (user.getSubEntity() != null) {
				managerName = user.getSubEntity().getManager().getName();
			} else {
				User manager = service.getConnectedUser();
				managerName = manager.getName();
			}

			Integer managerId = null;
			if (user.getSubEntity() != null) {
				managerId = user.getSubEntity().getManager().getId();
			} else {
				User manager = service.getConnectedUser();
				managerId = manager.getId();
			}

			List<SolutionResponse> solutions = null;
			if (user.getSolutions() != null) {
				solutions = user.getSolutions().stream().map(sol -> modelMapper.map(sol, SolutionResponse.class))
						.toList();
			}

			AddressResponse addressResponse = null;
			if (user.getAddress() != null) {
				addressResponse = modelMapper.map(user.getAddress(), AddressResponse.class);
			}

			List<WorkScheduleResponse> workScheduleResponses = null;
			if (user.getWorkSchedules() != null) {
				workScheduleResponses = user.getWorkSchedules().stream()
						.map(w -> modelMapper.map(w, WorkScheduleResponse.class)).toList();
			}
			Boolean deliveryBOYAvailable=true;
            if(user.getDeliveries()!=null) {
            	deliveryBOYAvailable=false;
            }
            
            Integer distanceOfDeliveryBoy=user.getDistanceOfDeliveryBoy();
			return new UserResponse(user.getId(), user.getAvatarPath(), user.getCivility(), user.getName(),
					user.getNationality(), user.getCin(), roleResponse, user.getEmail(), user.getPhone(),
					user.getRayon(), managerId, managerName, solutions, addressResponse, workScheduleResponses,
					user.getStatus(),deliveryBOYAvailable,distanceOfDeliveryBoy,user.getCoordinates());
		}, User.class, UserResponse.class);

		modelMapper.addMappings(new PropertyMap<User, ClientDto>() {
			@Override
			protected void configure() {
				map(source.getEmail(), destination.getEmail());
				map(source.getAccount().getProvider(), destination.getAccountProvider());
				map(source.getPhone(), destination.getPhoneNumber());
				map(source.getIsEmailVerified(), destination.isAccountVerified());
				map(source.getStatus(), destination.getAccountStatus());

			}
		});

	}
}
