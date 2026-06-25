package com.example.yahooapifinance.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yahooapifinance.services.IRapidStockService;
import com.example.yahooapifinance.services.IStockServiceJSON;

import ch.qos.logback.classic.Logger;

import java.util.List;

import com.example.yahooapifinance.models.StockJSON;


@RestController
@RequestMapping("/stocks")
public class StockController {

	@Lazy
	@Autowired
	private IRapidStockService rapidStockService;
	
	@Lazy
	@Autowired
	private IStockServiceJSON stockServiceJSON; 
	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(StockController.class);


    @GetMapping("/quota/{symbol}")
    public ResponseEntity<String> getStock(@PathVariable String symbol) {
    	logger.info("Se obtiene las acciones desde la API de Nokia RapidAPI que tiene informacion de Yahoo Finance");
    	logger.warn("No exceder en peticiones porque hay un limite gratuito");
    	
        String jsonResponse = rapidStockService.getStockDataFromRapidApi(symbol);
        return ResponseEntity.ok()
                             .header("Content-Type", "application/json")
                             .body(jsonResponse);
    }    
  
    @GetMapping("/all-stocks")
    public ResponseEntity<?> getStocksJSON() {
    	logger.info("Se obtiene las acciones desde el JSON estatico");
        List<StockJSON> stocks = stockServiceJSON.getStocks();
        return ResponseEntity.ok(stocks);
    }

}
