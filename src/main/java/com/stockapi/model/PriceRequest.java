package com.stockapi.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Price request object to be used within update operation
 * 
 * @author gorkemdemiray
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceRequest {
	
	private Long id;
	
	@NotNull(message = "Price can not be null!")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero!")
    @Digits(integer = 10, fraction = 2, message = "Illegal format for price!")
	private BigDecimal currentPrice;
}
