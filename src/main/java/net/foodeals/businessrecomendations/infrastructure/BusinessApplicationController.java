package net.foodeals.businessrecomendations.infrastructure;

import lombok.RequiredArgsConstructor;
import net.foodeals.businessrecomendations.application.dtos.requests.BusinessRegisterRequest;
import net.foodeals.businessrecomendations.application.dtos.responses.BusinessRegisterResponse;
import net.foodeals.businessrecomendations.application.service.BusinessApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/business")
@RequiredArgsConstructor
public class BusinessApplicationController {

    private final BusinessApplicationService service;

    @PostMapping("/register")
    public ResponseEntity<BusinessRegisterResponse> registerBusiness(
            @RequestBody BusinessRegisterRequest request) {
        return ResponseEntity.ok(service.registerBusiness(request));
    }
}