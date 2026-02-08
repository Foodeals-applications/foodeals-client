package net.foodeals.authentication.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppleUser {
    private String firstName;
    private String lastName;
    private String email;
    private String subject;
}
