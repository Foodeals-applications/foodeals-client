package net.foodeals.order.infrastructure.interfaces.web;

import net.foodeals.order.application.dtos.requests.PaymentRequest;
import net.foodeals.order.application.dtos.responses.PaymentResponse;
import net.foodeals.order.application.services.PayzoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PayzoneController {

    private final PayzoneService payzoneService;

    public PayzoneController(PayzoneService payzoneService) {
        this.payzoneService = payzoneService;
    }

    @PostMapping("/sale")
    public ResponseEntity<PaymentResponse> sale(@RequestBody PaymentRequest request) {
        PaymentResponse response = payzoneService.createPayment(request);
        return ResponseEntity.ok(response);
    }
}
