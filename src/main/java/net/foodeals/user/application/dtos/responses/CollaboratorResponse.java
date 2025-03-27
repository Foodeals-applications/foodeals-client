package net.foodeals.user.application.dtos.responses;

import net.foodeals.user.domain.enums.Civility;
import net.foodeals.user.domain.enums.Nationality;
import net.foodeals.user.domain.valueObjects.Name;

public record CollaboratorResponse (

		Integer id, String avatarPath, Civility civility, Name name, Nationality nationality, String cin,
		RoleResponse role, String email, String phone) {
}
