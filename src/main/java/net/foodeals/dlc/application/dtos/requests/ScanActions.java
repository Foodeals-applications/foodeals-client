package net.foodeals.dlc.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanActions {
    private boolean canClaim;
    private boolean canCreate;
}