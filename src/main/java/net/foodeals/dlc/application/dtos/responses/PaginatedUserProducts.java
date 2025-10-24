package net.foodeals.dlc.application.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedUserProducts {
    private List<UserProductResponse> items;
    private int page;
    private int limit;
    private int total;


    public PaginatedUserProducts(List<UserProductResponse> items, int page, int limit, int total) {
        this.items = items;
        this.page = page;
        this.limit = limit;
        this.total = total;
    }
}