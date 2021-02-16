package com.stockapi.exception;

/**
 * Exception to handle if stock is not found
 * 
 * @author gorkemdemiray
 *
 */
public class StockNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4023581634575635553L;

	public StockNotFoundException() {
		super();
	}

	public StockNotFoundException(String message) {
		super(message);
	}

}
