package com.stockapi.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Stock request object to be used within add operation
 * 
 * @author gorkemdemiray
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRequest {

	@NotNull(message = "Name can not be null!")
	@NotBlank(message = "Name can not be empty!")
	private String name;
	
	@NotNull(message = "Price can not be null!")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero!")
    @Digits(integer = 10, fraction = 2, message = "Illegal format for price!")
	private BigDecimal currentPrice;
}
