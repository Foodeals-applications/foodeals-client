package net.foodeals.authentication.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.foodeals.user.domain.valueObjects.Name;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ClientRegistredResponse {

    private Integer id ;
    private Name name;
    private String email;
    private String phone ;
    private UUID organizationEntityId;
    private List<String> solutions;
    private String role;
    private String avatarPath;
    private Integer id;
    private AuthenticationResponse token;
}
