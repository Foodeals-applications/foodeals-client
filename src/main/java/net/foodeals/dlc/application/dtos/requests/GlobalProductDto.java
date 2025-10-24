package net.foodeals.dlc.application.dtos.requests;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GlobalProductDto {
    private UUID id;
    private String barcode;
    private String name;
    private List<String> imageUrls;
}