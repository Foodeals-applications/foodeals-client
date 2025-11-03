package net.foodeals.user.application.dtos.requests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.core.domain.entities.Name;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InfosProfileRequest {
	
	
	private String avatarPath;
	
	private Name name;
	
	private String emailAddress;
	
	private String phone ;
	
	private String countryName;
	
	private String cityName ;
	
	private String zip ;
	
	private LocalDate birdhayDay;

}
