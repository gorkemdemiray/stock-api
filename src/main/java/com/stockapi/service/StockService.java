package com.stockapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockapi.domain.Stock;
import com.stockapi.exception.StockAlreadyExistsException;
import com.stockapi.exception.StockNotFoundException;
import com.stockapi.model.PriceRequest;
import com.stockapi.model.StockRequest;
import com.stockapi.model.StockResponse;
import com.stockapi.repository.StockRepository;

/**
 * Service to perform business logic
 * 
 * @author gorkemdemiray
 *
 */
@Service
public class StockService {
	
	@Autowired
	private StockRepository stockRepository;
	
	/**
	 * Gets list of all {@link Stock} entities and converts each of them to the
	 * {@link StockResponse} object
	 * 
	 * @return list of {@link StockResponse}
	 */
	public List<StockResponse> getStocks() {
		return stockRepository.findAll()
				.stream()
				.map(this::getStockResponse)
				.collect(Collectors.toList());
	}
	
	/**
	 * Gets {@link Stock} object with the given id and converts to the
	 * {@link StockResponse} if exists, otherwise throws exception
	 * 
	 * @param id - stock id
	 * @return {@link StockResponse}
	 */
	public StockResponse getStock(Long id) {
		Stock stock = find(id);
		return getStockResponse(stock);
	}
	
	/**
	 * Gets {@link Stock} entity if exists, otherwise throws exception
	 * 
	 * @param id - stock id
	 * @return {@link Stock}
	 */
	private Stock find(Long id) {
		return stockRepository.findById(id)
				.orElseThrow(() -> new StockNotFoundException("Stock not found with the id : " + id));
	}
	
	/**
	 * Creates new stock with the given name and current price and converts it to
	 * {@link StockResponse} object if all fields are valid and no stock exists with
	 * the name, otherwise throws exception
	 * 
	 * @param stockRequest - {@link StockRequest} which has name and current price
	 * @return {@link StockResponse}
	 */
	public StockResponse createStock(StockRequest stockRequest) {
		if (stockRepository.findByName(stockRequest.getName()).isPresent())
			throw new StockAlreadyExistsException("Stock already exists with the name : " + stockRequest.getName());
		Stock stock = Stock.builder()
				.name(stockRequest.getName())
				.currentPrice(stockRequest.getCurrentPrice())
				.lastUpdate(LocalDateTime.now()).build();
		Stock savedStock = stockRepository.save(stock);
		return getStockResponse(savedStock);
	}
	
	/**
	 * Updates the stock with the given current price and converts it to
	 * {@link StockResponse} object if fiels is valid, otherwise throws exception
	 * 
	 * @param stockId      - stock id
	 * @param priceRequest - {@link PriceRequest} which has current price
	 * @return {@link StockResponse}
	 */
	public StockResponse updateStock(Long stockId, PriceRequest priceRequest) {
		Stock stock = find(stockId);
		stock.setCurrentPrice(priceRequest.getCurrentPrice());
		Stock savedStock = stockRepository.save(stock);
		return getStockResponse(savedStock);
	}
	
	/**
	 * Converts {@link Stock} entity to {@link StockResponse} object
	 * 
	 * @param stock - {@link Stock}
	 * @return {@link StockResponse}
	 */
	private StockResponse getStockResponse(Stock stock) {
		if (stock == null)
			return null;

		StockResponse response = StockResponse.builder()
				.id(stock.getId())
				.name(stock.getName())
				.currentPrice(stock.getCurrentPrice())
				.lastUpdate(stock.getLastUpdate())
				.build();
		return response;
	}
}
