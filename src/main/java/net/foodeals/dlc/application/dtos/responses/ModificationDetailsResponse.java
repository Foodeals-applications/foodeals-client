package net.foodeals.dlc.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModificationDetailsResponse {

	private DlcResponse dlcResponse;
	private int oldQuantity;
	private int newQuantity;
	private int oldDiscount;
	private int newDiscount;
}
