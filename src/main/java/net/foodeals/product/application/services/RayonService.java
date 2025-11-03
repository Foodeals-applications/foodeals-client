package net.foodeals.product.application.services;

import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Rayon;
import net.foodeals.product.application.dtos.requests.RayonRequest;

public interface RayonService extends CrudService<Rayon, UUID, RayonRequest> {
}
