package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.foodeals.offer.domain.entities.Box;

import java.util.List;

@Data
@AllArgsConstructor
public class BoxListResponse {
    private List<BoxResponse> boxes;
    private long totalCount;
    private boolean hasMore;
}