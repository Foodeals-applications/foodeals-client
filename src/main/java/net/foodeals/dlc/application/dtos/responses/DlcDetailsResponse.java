package net.foodeals.dlc.application.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.user.application.dtos.responses.UserResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DlcDetailsResponse {

	private DlcResponse dlcResponse;
	private String expiryDate;
	private int modificationsCount;
	private int usersCount;
	private List<UserResponse> users;

	public DlcDetailsResponse(DlcResponse dlcResponse, int modificationsCount, int usersCount,
			List<UserResponse> users) {
		super();
		this.dlcResponse = dlcResponse;
		this.modificationsCount = modificationsCount;
		this.usersCount = usersCount;
		this.users = users;
	}

}