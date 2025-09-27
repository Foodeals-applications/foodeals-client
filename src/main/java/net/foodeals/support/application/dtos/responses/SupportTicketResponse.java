package net.foodeals.support.application.dtos.responses;

import lombok.Data;
import net.foodeals.support.domain.entities.SupportTicket;

import java.time.Instant;
import java.util.UUID;

@Data
public class SupportTicketResponse {
    private UUID id;
    private String subject;
    private String message;
    private String category;
    private String status;
    private Instant createdAt;

    public static SupportTicketResponse fromEntity(SupportTicket ticket) {
        SupportTicketResponse dto = new SupportTicketResponse();
        dto.setId(ticket.getId());
        dto.setSubject(ticket.getSubject());
        dto.setMessage(ticket.getMessage());
        dto.setCategory(ticket.getCategory());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());
        return dto;
    }}