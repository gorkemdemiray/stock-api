package com.stockapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Abstract REST controller test class to provide JSON object for add/update operations
 * 
 * @author gorkemdemiray
 *
 */
public class AbstractRestControllerTest {
	
	public static String asJsonString(final Object object) {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
