package net.foodeals.dlc.application.dtos.responses;

import lombok.Data;
import net.foodeals.dlc.application.dtos.requests.GlobalProductDto;
import net.foodeals.dlc.application.dtos.requests.ScanActions;

@Data
public class ScanLookupResponse {
    private boolean found;
    private GlobalProductDto globalProduct;
    private UserProductResponse userProduct;
    private ScanActions actions;
    private String message;
}