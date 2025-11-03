package net.foodeals.support.infrastructure;

import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.User;
import net.foodeals.support.application.dtos.requests.SupportTicketRequest;
import net.foodeals.support.application.dtos.responses.SupportTicketListResponse;
import net.foodeals.support.application.dtos.responses.SupportTicketResponse;
import net.foodeals.support.application.service.SupportTicketService;
import net.foodeals.user.application.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/support")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService service;
    private final UserService userService;

    @PostMapping("/ticket")
    public SupportTicketResponse createTicket(@RequestBody SupportTicketRequest request) {
        User user =userService.getConnectedUser();
        return service.createTicket(user, request);
    }

    @GetMapping("/tickets")
    public SupportTicketListResponse getTickets() {
        User user =userService.getConnectedUser();
        return service.getTickets(user);
    }}
