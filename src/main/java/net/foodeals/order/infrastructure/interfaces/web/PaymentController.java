package net.foodeals.order.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.Transaction;
import net.foodeals.order.domain.repositories.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService;
    private final TransactionRepository transactionRepository;

    @GetMapping("/pay")
    public ResponseEntity<String> processPayment( @RequestParam UUID idOrder) {
        Order order = orderService.findById(idOrder);
        Transaction transaction = order.getTransaction();
        transaction.setPaymentId(String.valueOf(UUID.randomUUID()));
        transactionRepository.save(transaction);
        return ResponseEntity.ok().body("Payment successful");

    }
}
