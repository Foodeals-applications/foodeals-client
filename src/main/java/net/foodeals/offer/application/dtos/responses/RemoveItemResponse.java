package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RemoveItemResponse {
    private boolean success;
    private String message;
    private Data data;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Data {
        private String removedItemId;
        private double updatedTotal;
    }
}