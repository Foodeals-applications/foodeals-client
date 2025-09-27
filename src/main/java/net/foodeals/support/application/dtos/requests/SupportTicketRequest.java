package net.foodeals.support.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketRequest {
    private String subject;
    private String message;
    private String category;
}