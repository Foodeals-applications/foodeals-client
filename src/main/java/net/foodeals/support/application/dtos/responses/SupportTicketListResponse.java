package net.foodeals.support.application.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class SupportTicketListResponse {
    private List<SupportTicketResponse> tickets;

    public SupportTicketListResponse(List<SupportTicketResponse> tickets) {
        this.tickets = tickets;
    }
}