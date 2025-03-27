package net.foodeals.dlc.application.dtos.responses;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.product.application.dtos.responses.ProductResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DlcResponse {

	private UUID id;

	private ProductResponse productResponse;
	private Date expiryDate;
	private Integer quantity;

	private String timeRemaining;

}
