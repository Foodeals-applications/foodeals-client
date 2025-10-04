package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreCartResponse {
    private UUID id;
    private String name;
    private String logoUrl;
    private Double total;
    private List<CartItemResponse> items;
}
