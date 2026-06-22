package com.example.yahooapifinance.services;

import java.io.IOException;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@Service
public class StockService {
	
	public Stock getStockDetails(String symbol) {
        try {
            // true flag fetches additional market statistics
            return YahooFinance.get(symbol, true); 
        } catch (IOException e) {
            System.err.println("Error fetching stock: " + e.getMessage());
            return null;
        }
    }
}
