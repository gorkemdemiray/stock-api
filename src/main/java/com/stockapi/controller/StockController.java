package com.stockapi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import com.stockapi.exception.StockAlreadyExistsException;
import com.stockapi.exception.StockNotFoundException;
import com.stockapi.model.PriceRequest;
import com.stockapi.model.StockRequest;
import com.stockapi.model.StockResponse;
import com.stockapi.service.StockService;

/**
 * MVC controller for Stock API
 * 
 * @author gorkemdemiray
 *
 */
@Controller
@RequestMapping
public class StockController {
	
	@Autowired
	private StockService stockService;

	/**
	 * Returns list of stock responses
	 * 
	 * @param model - contains list of all {@link StockResponse}
	 * @return view name
	 */
	@GetMapping({"", "/stocks/list"})
	public String getStocks(Model model) {
		model.addAttribute("stocks", stockService.getStocks());
		return "stocks/list";
	}
	
	/**
	 * Returns view for add operation
	 * 
	 * @param model - contains empty {@link StockRequest}
	 * @return view name
	 */
	@GetMapping("/stocks/add")
	public String createStock(Model model) {
		model.addAttribute("stock", new StockRequest());
		return "stocks/add";
	}
	
	/**
	 * Creates a new stock and redirects to the list page if all fields are valid,
	 * otherwise shows all the errors within the page
	 * 
	 * @param stockRequest  - {@link StockRequest} which has name and current price
	 * @param bindingResult - {@link BindingResult} to handle errors
	 * @return view name
	 */
	@PostMapping("/stocks/add")
	public String saveStock(@Valid @ModelAttribute("stock") StockRequest stockRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "stocks/add";
		stockService.createStock(stockRequest);
		return "redirect:/stocks/list";
	}
	
	/**
	 * Returns view for update operation
	 * 
	 * @param id    - stock id
	 * @param model - contains {@link StockResponse}
	 * @return view name
	 */
	@GetMapping("/stocks/update/{id}")
	public String updateStock(@PathVariable Long id, Model model) {
		StockResponse stockResponse = stockService.getStock(id);
		model.addAttribute("stock", stockResponse);
		return "stocks/update";
	}
	
	/**
	 * Updates the stock with the given id if current price field is valid,
	 * otherwise shows all the errors within the page
	 * 
	 * @param id            - stock id
	 * @param priceRequest  - {@link PriceRequest} which has current price
	 * @param bindingResult - {@link BindingResult} to handle errors
	 * @return view name
	 */
	@PostMapping("/stocks/update")
	public String updateStock(@RequestParam Long id, @Valid @ModelAttribute("stock") PriceRequest priceRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "stocks/update";
		stockService.updateStock(id, priceRequest);
		return "redirect:/stocks/list";
	}
	
	/**
	 * Handles StockNotFoundException exception and returns 404error page
	 * 
	 * @param exception - {@link StockNotFoundException}
	 * @return model and view object
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(StockNotFoundException.class)
	public ModelAndView handleNotFoundException(Exception exception) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("404error");
		modelAndView.addObject("exception", exception);
		return modelAndView;
	}
	
	/**
	 * Handles both MethodArgumentTypeMismatchException and
	 * StockAlreadyExistsException exceptions and returns 400error page
	 * 
	 * @param exception - {@link MethodArgumentTypeMismatchException} or
	 *                  {@link StockAlreadyExistsException}
	 * @return model and view object
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({MethodArgumentTypeMismatchException.class, StockAlreadyExistsException.class})
	public ModelAndView handleBadRequestException(Exception exception) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("400error");
		modelAndView.addObject("exception", exception);
		return modelAndView;
	}
}
