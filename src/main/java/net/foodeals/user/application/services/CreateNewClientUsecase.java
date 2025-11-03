package net.foodeals.user.application.services;

import net.foodeals.common.contracts.UseCase;
import net.foodeals.core.domain.entities.User;
import net.foodeals.user.application.dtos.requests.ClientRegisterRequest;

public interface CreateNewClientUsecase extends UseCase<ClientRegisterRequest, User> {
}
