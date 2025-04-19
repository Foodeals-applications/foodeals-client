package net.foodeals.authentication.application.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FacebookUser {

    private String id;
    private String name;
    private String email;
}
