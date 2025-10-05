package net.foodeals.order.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.order.application.dtos.requests.ProcessPaymentRequest;
import net.foodeals.order.application.dtos.responses.ProcessPaymentResponse;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.order.application.services.PaymentService;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.Transaction;
import net.foodeals.order.domain.repositories.TransactionRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService;
    private final TransactionRepository transactionRepository;
    private final PaymentService paymentService;
    private final UserService userService;

    @GetMapping("/pay")
    public ResponseEntity<String> processPayment( @RequestParam UUID idOrder) {
        Order order = orderService.findById(idOrder);
        Transaction transaction = order.getTransaction();
        transaction.setPaymentId(String.valueOf(UUID.randomUUID()));
        transactionRepository.save(transaction);
        return ResponseEntity.ok().body("Payment successful");

    }
    @PostMapping("/process")
    public ResponseEntity<ProcessPaymentResponse> processPayment(
            @RequestBody ProcessPaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    // 1️⃣ Get Payment Data
    @GetMapping("/data")
    public Map<String, Object> getPaymentData() {
        User user =userService.getConnectedUser();
        return paymentService.getPaymentData(user.getId());
    }

    // 2️⃣ Select Payment Method for Store
    @PutMapping("/store/method")
    public Map<String, Object> selectPaymentMethod(@RequestBody Map<String, String> request) {
        return paymentService.selectStorePaymentMethod(request);
    }

    // 3️⃣ Apply Store Promo Code
    @PostMapping("/promo/store")
    public Map<String, Object> applyStorePromo(@RequestBody Map<String, String> request) {
        return paymentService.applyStorePromo(request);
    }

    // 4️⃣ Apply Global Promo
    @PostMapping("/promo/global")
    public Map<String, Object> applyGlobalPromo(@RequestBody Map<String, String> request) {
        return paymentService.applyGlobalPromo(request);
    }

    // 5️⃣ Pay for Single Store
    @PostMapping("/store/pay")
    public Map<String, Object> payForStore(@RequestBody Map<String, Object> request) {
        return paymentService.payForStore(request);
    }

    // 6️⃣ Pay for All Selected Items (Global)
    @PostMapping("/global/pay")
    public Map<String, Object> payGlobal(@RequestBody Map<String, Object> request) {
        return paymentService.payGlobal(request);
    }

    // 7️⃣ Get Payment Summary
    @GetMapping("/summary")
    public Map<String, Object> getPaymentSummary() {
        User user=userService.getConnectedUser();
        return paymentService.getPaymentSummary(user.getId());
    }

    // 8️⃣ Validate Payment
    @PostMapping("/validate")
    public Map<String, Object> validatePayment(@RequestBody Map<String, Object> request) {
        return paymentService.validatePayment(request);
    }

    // 9️⃣ Get Available Payment Methods
    @GetMapping("/methods")
    public Map<String, Object> getPaymentMethods() {
        User user=userService.getConnectedUser();
        return paymentService.getPaymentMethods(user.getId());
    }

    // 🔟 Apply Loyalty Points
    @PostMapping("/loyalty/apply")
    public Map<String, Object> applyLoyalty(@RequestBody Map<String, Object> request) {
        return paymentService.applyLoyalty(request);
    }
}
