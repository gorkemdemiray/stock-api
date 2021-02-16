package com.stockapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.stockapi.advice.StockExceptionHandler;
import com.stockapi.exception.StockAlreadyExistsException;
import com.stockapi.exception.StockNotFoundException;
import com.stockapi.model.PriceRequest;
import com.stockapi.model.StockRequest;
import com.stockapi.model.StockResponse;
import com.stockapi.service.StockService;

/**
 * REST Controller unit tests
 * 
 * @author gorkemdemiray
 *
 */
public class StockControllerUnitTest extends AbstractRestControllerTest {
	
	@InjectMocks
	private StockRestController stockController;

	@Mock
	private StockService stockService;
	
	private MockMvc mockMvc;
	
	private StockResponse gme, amc, bb, nok, tsla, appl;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(stockController).setControllerAdvice(new StockExceptionHandler()).build();
		
		gme = StockResponse.builder().name("GameStop Corp.").currentPrice(new BigDecimal("325.00"))
				.lastUpdate(LocalDateTime.now()).build();
		amc = StockResponse.builder().name("AMC Entertainment Holdings Inc").currentPrice(new BigDecimal("13.26"))
				.lastUpdate(LocalDateTime.now()).build();
		bb = StockResponse.builder().name("BlackBerry Ltd").currentPrice(new BigDecimal("14.10"))
				.lastUpdate(LocalDateTime.now()).build();
		nok = StockResponse.builder().name("Nokia Oyj").currentPrice(new BigDecimal("4.56"))
				.lastUpdate(LocalDateTime.now()).build();
		tsla = StockResponse.builder().name("Tesla Inc").currentPrice(new BigDecimal("793.53"))
				.lastUpdate(LocalDateTime.now()).build();
		appl = StockResponse.builder().name("Apple Inc").currentPrice(new BigDecimal("131.96"))
				.lastUpdate(LocalDateTime.now()).build();
	}
	
	@Test
	public void getStocks() throws Exception {
		when(stockService.getStocks()).thenReturn(Arrays.asList(gme, amc, bb, nok, tsla));
		
		mockMvc.perform(get("/api/stocks")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)));
	}
	
	@Test
	public void getStock() throws Exception {
		when(stockService.getStock(anyLong())).thenReturn(tsla);
		
		mockMvc.perform(get("/api/stocks/5")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(tsla.getName()))
				.andExpect(jsonPath("$.currentPrice").value(tsla.getCurrentPrice()));
	}
	
	@Test
	public void getStockWithInvalidId() throws Exception {
		when(stockService.getStock(anyLong())).thenThrow(StockNotFoundException.class);
		
		Exception exception = mockMvc.perform(get("/api/stocks/8")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(StockNotFoundException.class);
	}
	
	@Test
	public void getStockWithInvalidArgument() throws Exception {
		when(stockService.getStock(anyLong())).thenThrow(MethodArgumentTypeMismatchException.class);
		
		Exception exception = mockMvc.perform(get("/api/stocks/xyz")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentTypeMismatchException.class);
	}
	
	@Test
	public void createStock() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name(appl.getName())
				.currentPrice(appl.getCurrentPrice())
				.build();
		
		when(stockService.createStock(stockRequest)).thenReturn(appl);
		
		mockMvc.perform(post("/api/stocks").contentType(MediaType.APPLICATION_JSON).content(asJsonString(stockRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value(appl.getName()))
				.andExpect(jsonPath("$.currentPrice").value(appl.getCurrentPrice()));
	}
	
	@Test
	public void createStockAlreadyExists() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name(tsla.getName())
				.currentPrice(tsla.getCurrentPrice())
				.build();
		
		when(stockService.createStock(stockRequest)).thenThrow(StockAlreadyExistsException.class);
		
		Exception exception = mockMvc.perform(post("/api/stocks").contentType(MediaType.APPLICATION_JSON).content(asJsonString(stockRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(StockAlreadyExistsException.class);
	}
	
	@Test
	public void updateStock() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(new BigDecimal("450.75"))
				.build();
		
		when(stockService.updateStock(anyLong(), any(PriceRequest.class))).thenReturn(tsla);
		
		mockMvc.perform(put("/api/stocks/5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(priceRequest)))
				.andExpect(status().isNoContent())
				.andExpect(jsonPath("$.id").value(tsla.getId()))
				.andExpect(jsonPath("$.currentPrice").value(tsla.getCurrentPrice()));
	}
	
	@Test
	public void updateStockWithInvalidId() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(tsla.getCurrentPrice())
				.build();
		
		when(stockService.updateStock(anyLong(), any(PriceRequest.class))).thenThrow(StockNotFoundException.class);
		
		Exception exception = mockMvc.perform(put("/api/stocks/8")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(priceRequest)))
				.andExpect(status().isNotFound())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(StockNotFoundException.class);
	}
	
	@Test
	public void updateStockWithInvalidArgument() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(tsla.getCurrentPrice())
				.build();
		
		when(stockService.updateStock(anyLong(), any(PriceRequest.class))).thenThrow(MethodArgumentTypeMismatchException.class);
		
		Exception exception = mockMvc.perform(get("/api/stocks/xyz")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(priceRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentTypeMismatchException.class);
	}
}
