package net.foodeals.referals.application.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class ReferralInviteRequest {
    private List<String> emails;
    private String message; // optional
}