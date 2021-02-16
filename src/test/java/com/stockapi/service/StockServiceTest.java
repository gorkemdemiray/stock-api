package com.stockapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.stockapi.domain.Stock;
import com.stockapi.exception.StockAlreadyExistsException;
import com.stockapi.exception.StockNotFoundException;
import com.stockapi.model.PriceRequest;
import com.stockapi.model.StockRequest;
import com.stockapi.model.StockResponse;
import com.stockapi.repository.StockRepository;

/**
 * Service unit tests
 * 
 * @author gorkemdemiray
 *
 */
public class StockServiceTest {
	
	@InjectMocks
	private StockService stockService;

	@Mock
	private StockRepository stockRepository;
	
	private Stock gme, amc, bb, nok, tsla, appl;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		
		gme = Stock.builder().id(1L).name("GameStop Corp.").currentPrice(new BigDecimal("325.00"))
				.lastUpdate(LocalDateTime.now()).build();
		amc = Stock.builder().id(2L).name("AMC Entertainment Holdings Inc").currentPrice(new BigDecimal("13.26"))
				.lastUpdate(LocalDateTime.now()).build();
		bb = Stock.builder().id(3L).name("BlackBerry Ltd").currentPrice(new BigDecimal("14.10"))
				.lastUpdate(LocalDateTime.now()).build();
		nok = Stock.builder().id(4L).name("Nokia Oyj").currentPrice(new BigDecimal("4.56"))
				.lastUpdate(LocalDateTime.now()).build();
		tsla = Stock.builder().id(5L).name("Tesla Inc").currentPrice(new BigDecimal("793.53"))
				.lastUpdate(LocalDateTime.now()).build();
		appl = Stock.builder().name("Apple Inc").currentPrice(new BigDecimal("131.96")).lastUpdate(LocalDateTime.now())
				.build();
	}
	
	@Test
	public void getStocks() throws Exception {
		when(stockRepository.findAll()).thenReturn(Arrays.asList(gme, amc, bb, nok, tsla));
		
		List<StockResponse> stockListResponse = stockService.getStocks();
		
		assertEquals(5, stockListResponse.size());
	}
	
	@Test
	public void getStock() throws Exception {
		when(stockRepository.findById(anyLong())).thenReturn(Optional.ofNullable(tsla));
		
		StockResponse stockResponse = stockService.getStock(tsla.getId());
		
		assertEquals(stockResponse.getId(), tsla.getId());
		assertEquals(stockResponse.getName(), tsla.getName());
		assertEquals(stockResponse.getCurrentPrice(), tsla.getCurrentPrice());
	}
	
	@Test
	public void getStockWithInvalidId() throws Exception {
		when(stockRepository.findById(anyLong())).thenThrow(StockNotFoundException.class);
		
		assertThrows(StockNotFoundException.class, () -> stockService.getStock(8L));
	}
	
	@Test
	public void getStockWithInvalidArgument() throws Exception {
		when(stockRepository.findById(anyLong())).thenThrow(NumberFormatException.class);
		
		assertThrows(NumberFormatException.class, () -> stockService.getStock(Long.valueOf("xyz")));
	}
	
	@Test
	public void createStock() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name(appl.getName())
				.currentPrice(appl.getCurrentPrice())
				.build();

		when(stockRepository.findByName(anyString())).thenReturn(Optional.empty());
		when(stockRepository.save(any(Stock.class))).thenReturn(appl);
		
		StockResponse stockResponse = stockService.createStock(stockRequest);
		
		assertEquals(stockResponse.getId(), appl.getId());
		assertEquals(stockResponse.getName(), appl.getName());
		assertEquals(stockResponse.getCurrentPrice(), appl.getCurrentPrice());
	}
	
	@Test
	public void createStockAlreadyExists() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name(tsla.getName())
				.currentPrice(tsla.getCurrentPrice())
				.build();
		
		when(stockRepository.findByName(anyString())).thenReturn(Optional.ofNullable(tsla));
		
		assertThrows(StockAlreadyExistsException.class, () -> stockService.createStock(stockRequest));
	}
	
	@Test
	public void updateStock() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(new BigDecimal("450.75"))
				.build();
		
		when(stockRepository.findById(anyLong())).thenReturn(Optional.ofNullable(tsla));
		when(stockRepository.save(any(Stock.class))).thenReturn(tsla);
		
		StockResponse stockResponse = stockService.updateStock(tsla.getId(), priceRequest);
		
		assertEquals(stockResponse.getId(), tsla.getId());
		assertEquals(stockResponse.getName(), tsla.getName());
		assertEquals(stockResponse.getCurrentPrice(), tsla.getCurrentPrice());
	}
	
	@Test
	public void updateStockWithInvalidId() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(tsla.getCurrentPrice())
				.build();
		
		when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		assertThrows(StockNotFoundException.class, () -> stockService.updateStock(8L, priceRequest));
	}
	
	@Test
	public void updateStockWithInvalidArgument() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(tsla.getCurrentPrice())
				.build();
		
		when(stockRepository.findById(anyLong())).thenThrow(NumberFormatException.class);
		
		assertThrows(NumberFormatException.class, () -> stockService.updateStock(Long.valueOf("xyz"), priceRequest));
	}
}
