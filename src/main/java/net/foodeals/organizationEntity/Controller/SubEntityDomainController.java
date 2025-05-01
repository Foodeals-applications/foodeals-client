package net.foodeals.organizationEntity.Controller;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityDomainResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityProductCategoryResponse;
import net.foodeals.organizationEntity.application.services.SubEntityDomainService;
import net.foodeals.organizationEntity.application.services.SubEntityProductCategoryService;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/subentities-domains")
@RequiredArgsConstructor
public class SubEntityDomainController {

    private final SubEntityDomainService subEntityDomainService;


    @GetMapping
    public ResponseEntity<List<SubEntityDomainResponse>> findAll() {

        return ResponseEntity.ok(subEntityDomainService.findAll());
    }

    @GetMapping("/list-categories-by-domain/{id}")
    public ResponseEntity<List<SubEntityProductCategoryResponse>> getCategoriesOfDomains(@PathVariable UUID id) {
        List<SubEntityProductCategoryResponse> responses =subEntityDomainService.findSubEntityProductCategoriesBySubEntityDomain(id);
        return ResponseEntity.ok(responses);
    }
}
