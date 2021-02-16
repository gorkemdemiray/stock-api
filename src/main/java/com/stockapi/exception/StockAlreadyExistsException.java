package com.stockapi.exception;

/**
 * Exception to handle if stock already exists
 * 
 * @author gorkemdemiray
 *
 */
public class StockAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 4023581634575635553L;

	public StockAlreadyExistsException() {
		super();
	}

	public StockAlreadyExistsException(String message) {
		super(message);
	}

}
