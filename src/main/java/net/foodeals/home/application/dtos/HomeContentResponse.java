package net.foodeals.home.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeContentResponse {
    private HomeFilteredContent content;
    private boolean hasMore;
}