package net.foodeals.organizationEntity.application.dtos.responses;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.location.application.dtos.responses.AddressResponse;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus;
import net.foodeals.user.application.dtos.responses.UserResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SubEntityResponse {
	private UUID id;
	private String name;
	private List<ActivityResponse> activities;
	private String avatarPath;
	private String coverPath;
	private String email;
	private String phone ;
	private List<SolutionResponse>solutions;
	private AddressResponse addressReponse;
	private String iframe ;
	private Date creationDate;
	private int numberOfCollabs;
    private int numberOfOffers ;
    private int numberOfOrders;
    private UserResponse manager ;
    private SubEntityStatus status;
}
