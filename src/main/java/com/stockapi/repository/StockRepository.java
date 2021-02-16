package com.stockapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockapi.domain.Stock;

/**
 * {@link Stock} repository to handle CRUD operations
 * 
 * @author gorkemdemiray
 *
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

	public Optional<Stock> findByName(String name);
}
