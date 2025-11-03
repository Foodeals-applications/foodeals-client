package net.foodeals.user.application.dtos.responses;


import net.foodeals.core.domain.entities.Name;
import net.foodeals.core.domain.enums.Civility;
import net.foodeals.core.domain.enums.Nationality;

public record CollaboratorResponse (

        Integer id, String avatarPath, Civility civility, Name name, Nationality nationality, String cin,
        RoleResponse role, String email, String phone) {
}
