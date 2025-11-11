package net.foodeals.order.application.services;

import net.foodeals.config.PayzoneProperties;
import net.foodeals.order.application.dtos.requests.PaymentRequest;
import net.foodeals.order.application.dtos.responses.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PayzoneService {

    private final RestTemplate restTemplate;
    private final PayzoneProperties props;

    public PayzoneService(RestTemplate restTemplate, PayzoneProperties props) {
        this.restTemplate = restTemplate;
        this.props = props;
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("merchantId", props.getMerchantId());
        payload.put("password", props.getPassword());
        payload.put("amount", request.getAmount());
        payload.put("currency", request.getCurrency());
        payload.put("orderId", request.getOrderId());
        payload.put("callbackUrl", props.getCallbackUrl());

        String url = props.getApiUrl() + "/transaction/sale/creditcard";

        ResponseEntity<PaymentResponse> response =
                restTemplate.postForEntity(url, payload, PaymentResponse.class);

        return response.getBody();
    }
}
