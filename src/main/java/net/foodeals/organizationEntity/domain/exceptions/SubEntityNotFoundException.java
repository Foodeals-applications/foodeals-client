package net.foodeals.organizationEntity.domain.exceptions;

import java.util.UUID;

public class SubEntityNotFoundException extends RuntimeException{
    private final UUID id;

    public SubEntityNotFoundException(UUID id) {
        super("SubEntity with id " + id + " not found");
        this.id = id;
    }
}
