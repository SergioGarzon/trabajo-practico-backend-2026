package com.example.yahooapifinance.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;


@RestController
public class StockController {

	@GetMapping("/saludo")
	public String saludo() {
		
		try {
			String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
			Map<String, Stock> stocks;
			stocks = YahooFinance.get(symbols);
			Stock intel = stocks.get("INTC");
			Stock airbus = stocks.get("AIR.PA");
			
			return intel + "";
		} catch (IOException e) {
			
			e.printStackTrace();
			return "Error dio";
		} 
		
	}
}
