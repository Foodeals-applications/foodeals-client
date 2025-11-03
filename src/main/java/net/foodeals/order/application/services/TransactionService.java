package net.foodeals.order.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Transaction;
import net.foodeals.order.application.dtos.requests.TransactionRequest;

import java.util.UUID;

public interface TransactionService extends CrudService<Transaction, UUID, TransactionRequest> {
}
