package net.foodeals.order.application.services;

import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.order.application.dtos.requests.ProcessPaymentRequest;
import net.foodeals.order.application.dtos.responses.ProcessPaymentResponse;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.Transaction;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.TransactionStatus;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.order.domain.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    public ProcessPaymentResponse processPayment(ProcessPaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ⚡ Ici logique de paiement (Stripe, PayPal, etc.)
        Transaction tx = new Transaction();
        tx.setOrder(order);
        BigDecimal amount=new BigDecimal(request.getAmount().toString());
        tx.setPrice(new Price(amount, Currency.getInstance("MAD")));
        tx.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(tx);

        order.setTransaction(tx);
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        return new ProcessPaymentResponse(tx.getId(), "SUCCESS", "Payment processed successfully");
    }}