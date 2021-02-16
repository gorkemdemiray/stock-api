package com.stockapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockapi.model.PriceRequest;
import com.stockapi.model.StockRequest;
import com.stockapi.model.StockResponse;
import com.stockapi.service.StockService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST controller for Stock API
 * 
 * @author gorkemdemiray
 *
 */
@RestController
@RequestMapping("/api/stocks")
@Api(value = "Stock API")
public class StockRestController {
	
	@Autowired
	private StockService stockService;

	/**
	 * Returns list of stock responses
	 * 
	 * @return list of all {@link StockResponse}
	 */
	@GetMapping
	@ApiOperation(value = "Gets all the stock list")
	public ResponseEntity<List<StockResponse>> getStocks() {
		return ResponseEntity.ok().body(stockService.getStocks());
	}
	
	/**
	 * Returns stock response
	 * 
	 * @param id - stock id
	 * @return {@link StockResponse} due to given id
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "Gets the stock due to the given id")
	public ResponseEntity<StockResponse> getStock(@PathVariable Long id) {
		return ResponseEntity.ok().body(stockService.getStock(id));
	}
	
	/**
	 * Adds a new stock with given values if all fields are valid, otherwise throws
	 * exception
	 * 
	 * @param stockRequest - {@link StockRequest} which has name and current price
	 * @return {@link StockResponse}
	 */
	@PostMapping
	@ApiOperation(value = "Creates a new stock with the given values", notes = "Stock name and current price should be valid")
	public ResponseEntity<StockResponse> createStock(@Valid @RequestBody StockRequest stockRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(stockService.createStock(stockRequest));
	}
	
	/**
	 * Updates the stock price if field is valid, otherwise throws exception
	 * 
	 * @param id           - stock id
	 * @param priceRequest {@link PriceRequest} which has current price
	 * @return {@link StockResponse}
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "Updates current price of the stock with the given value", notes = "Current price should be valid")
	public ResponseEntity<StockResponse> updateStock(@PathVariable Long id, @Valid @RequestBody PriceRequest priceRequest) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(stockService.updateStock(id, priceRequest));
	}
}
