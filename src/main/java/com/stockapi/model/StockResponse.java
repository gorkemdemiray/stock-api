package com.stockapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Stock response object to be used as a response for all endpoints
 * 
 * @author gorkemdemiray
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {

	private Long id;
	private String name;
	private BigDecimal currentPrice;
	private LocalDateTime lastUpdate;
}