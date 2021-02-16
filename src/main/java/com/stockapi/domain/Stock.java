package com.stockapi.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Stock entity object
 * 
 * @author gorkemdemiray
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stock {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private BigDecimal currentPrice;
	private LocalDateTime lastUpdate;
}
