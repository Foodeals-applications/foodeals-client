package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoxListResponse {
    private List<BoxResponse> boxes;
    private long totalCount;
    private boolean hasMore;
}