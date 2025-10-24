package net.foodeals.dlc.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProductResponse {
    private UUID id;
    private String userId; // left null if not wired to auth in this POC
    private UUID productId;
    private String name;
    private String barcode;
    private Integer quantity;
    private String unit = "pcs";
    private Double price;
    private String status; // suitable/exige/urgent
    private Boolean notifyOnStatusChange = true;
    private String note;
    private String location;
    private Date expiresAt;
    private List<String> imageUrls;
    private Date createdAt;
    private Date updatedAt;
}