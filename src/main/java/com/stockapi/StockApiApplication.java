package com.stockapi;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.stockapi.domain.Stock;
import com.stockapi.repository.StockRepository;

/**
 * Runner class for Stock API
 * 
 * @author gorkemdemiray
 *
 */
@SpringBootApplication
public class StockApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(StockApiApplication.class, args);
	}
	
	@Autowired
	private StockRepository stockRepository;

	@Override
	public void run(String... args) throws Exception {
		loadStocks();
	}
	
	/**
	 * Constructs {@link Stock} entities on application startup
	 */
	private void loadStocks() {
		Stock gme = Stock.builder().name("GameStop Corp.").currentPrice(new BigDecimal("325.00")).lastUpdate(LocalDateTime.now()).build();
		stockRepository.save(gme);
		
		Stock amc = Stock.builder().name("AMC Entertainment Holdings Inc").currentPrice(new BigDecimal("13.26")).lastUpdate(LocalDateTime.now()).build();
		stockRepository.save(amc);
		
		Stock bb = Stock.builder().name("BlackBerry Ltd").currentPrice(new BigDecimal("14.10")).lastUpdate(LocalDateTime.now()).build();
		stockRepository.save(bb);
		
		Stock nok = Stock.builder().name("Nokia Oyj").currentPrice(new BigDecimal("4.56")).lastUpdate(LocalDateTime.now()).build();
		stockRepository.save(nok);
		
		Stock tsla = Stock.builder().name("Tesla Inc").currentPrice(new BigDecimal("793.53")).lastUpdate(LocalDateTime.now()).build();
		stockRepository.save(tsla);
	}

}
