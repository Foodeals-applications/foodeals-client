package net.foodeals.product.application.services;

import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.RayonRequest;
import net.foodeals.product.domain.entities.Rayon;

public interface RayonService extends CrudService<Rayon, UUID, RayonRequest> {
}
