package com.stockapi.advice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.stockapi.exception.StockAlreadyExistsException;
import com.stockapi.exception.StockNotFoundException;

/**
 * Handles exceptions and returns human readable {@code NOT_FOUND} and {@code BAD_REQUEST} responses.
 * 
 * @author gorkemdemiray
 * 
 */
@ControllerAdvice
public class StockExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handles StockNotFoundException exception and returns error response
	 * 
	 * @param exception - {@link StockNotFoundException}
	 * @param request   - {@link WebRequest}
	 * @return {@link ErrorResponse}
	 */
	@ExceptionHandler(StockNotFoundException.class)
	public ResponseEntity<Object> handleNotFoundException(Exception exception, WebRequest request) {
		ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
		return new ResponseEntity<Object>(response, new HttpHeaders(), response.getStatus());
	}
	
	/**
	 * Handles both MethodArgumentTypeMismatchException and
	 * StockAlreadyExistsException exceptions and returns error response
	 * 
	 * @param exception - {@link MethodArgumentTypeMismatchException} or
	 *                  {@link StockAlreadyExistsException}
	 * @param request   - {@link WebRequest}
	 * @return {@link ErrorResponse}
	 */
	@ExceptionHandler({MethodArgumentTypeMismatchException.class, StockAlreadyExistsException.class})
	public ResponseEntity<Object> handleBadRequestException(Exception exception, WebRequest request) {
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
		return new ResponseEntity<Object>(response, new HttpHeaders(), response.getStatus());
	}

	/**
	 * Handles MethodArgumentNotValidException exception and returns error response
	 * 
	 * @param exception - {@link MethodArgumentNotValidException}
	 * @param headers   - {@link HttpHeaders}
	 * @param status    - {@link HttpStatus}
	 * @param request   - {@link WebRequest}
	 * @return {@link ErrorResponse}
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errorMessage = new ArrayList<>();
		exception.getBindingResult().getAllErrors().forEach(error -> errorMessage.add(error.getDefaultMessage()));
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
		return new ResponseEntity<Object>(response, new HttpHeaders(), response.getStatus());
	}
	
}
