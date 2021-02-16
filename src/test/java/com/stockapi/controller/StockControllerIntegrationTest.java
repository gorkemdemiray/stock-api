package com.stockapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.stockapi.exception.StockAlreadyExistsException;
import com.stockapi.exception.StockNotFoundException;
import com.stockapi.model.PriceRequest;
import com.stockapi.model.StockRequest;

/**
 * REST Controller integration tests
 * 
 * @author gorkemdemiray
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
public class StockControllerIntegrationTest extends AbstractRestControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void getStocks() throws Exception {
		mockMvc.perform(get("/api/stocks")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)));
	}
	
	@Test
	public void getStock() throws Exception {
		mockMvc.perform(get("/api/stocks/5")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Tesla Inc"))
				.andExpect(jsonPath("$.currentPrice").value(new BigDecimal("793.53")));
	}
	
	@Test
	public void getStockWithInvalidId() throws Exception {
		Exception exception = mockMvc.perform(get("/api/stocks/8")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(StockNotFoundException.class);
	}
	
	@Test
	public void getStockWithInvalidArgument() throws Exception {
		Exception exception = mockMvc.perform(get("/api/stocks/xyz")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentTypeMismatchException.class);
	}
	
	@Test
	@DirtiesContext
	public void createStock() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name("Apple Inc")
				.currentPrice(new BigDecimal("131.96"))
				.build();
		mockMvc.perform(post("/api/stocks").contentType(MediaType.APPLICATION_JSON).content(asJsonString(stockRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value(stockRequest.getName()))
				.andExpect(jsonPath("$.currentPrice").value(stockRequest.getCurrentPrice()));
	}
	
	@Test
	public void createStockAlreadyExists() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name("Tesla Inc")
				.currentPrice(new BigDecimal("857.93"))
				.build();
		Exception exception = mockMvc.perform(post("/api/stocks").contentType(MediaType.APPLICATION_JSON).content(asJsonString(stockRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(StockAlreadyExistsException.class);
	}
	
	@Test
	public void createStockWithNullName() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name(null)
				.currentPrice(new BigDecimal("857.93"))
				.build();
		Exception exception = mockMvc.perform(post("/api/stocks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(stockRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentNotValidException.class);
	}
	
	@Test
	public void createStockWithEmptyName() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name("")
				.currentPrice(new BigDecimal("857.93"))
				.build();
		Exception exception = mockMvc.perform(post("/api/stocks").contentType(MediaType.APPLICATION_JSON).content(asJsonString(stockRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentNotValidException.class);
	}
	
	@Test
	public void createStockWithNullPrice() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name("Apple Inc")
				.currentPrice(null)
				.build();
		Exception exception = mockMvc.perform(post("/api/stocks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(stockRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentNotValidException.class);
	}
	
	@Test
	public void createStockWithZeroPrice() throws Exception {
		StockRequest stockRequest = StockRequest.builder()
				.name("Apple Inc")
				.currentPrice(new BigDecimal("0.00"))
				.build();
		Exception exception = mockMvc.perform(post("/api/stocks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(stockRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentNotValidException.class);
	}
	
	@Test
	public void updateStock() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(new BigDecimal("450.75"))
				.build();
		mockMvc.perform(put("/api/stocks/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(priceRequest)))
				.andExpect(status().isNoContent())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.currentPrice").value(priceRequest.getCurrentPrice()));
	}
	
	@Test
	public void updateStockWithInvalidId() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(new BigDecimal("450.75"))
				.build();
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
				.currentPrice(new BigDecimal("450.75"))
				.build();
		Exception exception = mockMvc.perform(get("/api/stocks/xyz")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(priceRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentTypeMismatchException.class);
	}
	
	@Test
	public void updateStockWithNullPrice() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(null)
				.build();
		Exception exception = mockMvc.perform(put("/api/stocks/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(priceRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentNotValidException.class);
	}
	
	@Test
	public void updateStockWithZeroPrice() throws Exception {
		PriceRequest priceRequest = PriceRequest.builder()
				.currentPrice(new BigDecimal("0.00"))
				.build();
		Exception exception = mockMvc.perform(put("/api/stocks/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(priceRequest)))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResolvedException();
		
		assertThat(exception).isExactlyInstanceOf(MethodArgumentNotValidException.class);
	}
}
