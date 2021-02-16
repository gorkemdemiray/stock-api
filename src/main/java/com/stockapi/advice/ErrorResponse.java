package com.stockapi.advice;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * Error response for exception handler
 * 
 * @author gorkemdemiray
 *
 */
@Data
public class ErrorResponse {

	private HttpStatus status;
    private List<String> errorMessage;

    public ErrorResponse(HttpStatus status, List<String> errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(HttpStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = Arrays.asList(errorMessage);
    }
}
