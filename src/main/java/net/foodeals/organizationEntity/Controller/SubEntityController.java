package net.foodeals.organizationEntity.Controller;

import java.util.UUID;

import net.foodeals.organizationEntity.application.dtos.responses.SubEntityDetailsResponse;
import net.foodeals.user.application.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.services.SubEntityService;

@RestController
@RequestMapping("v1/subentities")
@RequiredArgsConstructor
public class SubEntityController {

    private final SubEntityService subEntityService;

    private final UserService userService;

    @GetMapping("/{id}/details")
    public ResponseEntity<SubEntityDetailsResponse> getSubEntityDetails(@PathVariable UUID id) {
        Integer userId=userService.getConnectedUser().getId();
        SubEntityDetailsResponse response = subEntityService.getSubEntityDetails(id, userId);
        return ResponseEntity.ok(response);
    }


}
