package net.foodeals.authentication.application.dtos.responses;


import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.foodeals.core.domain.entities.Name;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Name name;
    private String email;
    private String phone ;
    private UUID organizationEntityId;
    private List<String>solutions;
    private String role;
    private String avatarPath;
    private Integer id;
    private AuthenticationResponse token;
}
