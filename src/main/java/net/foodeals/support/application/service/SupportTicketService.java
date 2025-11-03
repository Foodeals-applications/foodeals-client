package net.foodeals.support.application.service;

import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.SupportTicket;
import net.foodeals.core.domain.entities.User;
import net.foodeals.core.repositories.SupportTicketRepository;
import net.foodeals.support.application.dtos.requests.SupportTicketRequest;
import net.foodeals.support.application.dtos.responses.SupportTicketListResponse;
import net.foodeals.support.application.dtos.responses.SupportTicketResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository repository;

    public SupportTicketResponse createTicket(User user, SupportTicketRequest request) {
        SupportTicket ticket = new SupportTicket();
        ticket.setUser(user);
        ticket.setSubject(request.getSubject());
        ticket.setMessage(request.getMessage());
        ticket.setCategory(request.getCategory());
        ticket.setStatus("OPEN");
        SupportTicket saved = repository.save(ticket);
        return SupportTicketResponse.fromEntity(saved);
    }

    public SupportTicketListResponse getTickets(User user) {
        List<SupportTicketResponse> list = repository.findByUser(user)
                .stream()
                .map(SupportTicketResponse::fromEntity)
                .toList();
        return new SupportTicketListResponse(list);
    }}