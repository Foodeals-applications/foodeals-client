package net.foodeals.organizationEntity.Controller;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.services.SubEntityService;

@RestController
@RequestMapping("v1/subentities")
@RequiredArgsConstructor
public class SubEntityController {

	private final SubEntityService service;
	private final ModelMapper mapper;

	@Autowired
    private SubEntityService subentityService;

    /*@GetMapping("/details")
    public ResponseEntity<SubentityDetailsDTO> getSubentityDetails(@RequestParam UUID subentityId) {
        SubentityDetailsDTO subentityDetails = subentityService.getSubentityDetails(subentityId);
        return ResponseEntity.ok(subentityDetails);
    }*/
}
