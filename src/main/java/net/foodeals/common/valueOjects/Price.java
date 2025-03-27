package net.foodeals.common.valueOjects;

import java.math.BigDecimal;
import java.util.Currency;

import jakarta.persistence.Embeddable;

@Embeddable
public record Price(BigDecimal amount, Currency currency) {
	
}
