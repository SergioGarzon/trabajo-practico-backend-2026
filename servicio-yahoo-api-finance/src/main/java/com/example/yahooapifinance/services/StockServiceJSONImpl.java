package com.example.yahooapifinance.services;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.yahooapifinance.models.StockJSON;

@Lazy
@Service
public class StockServiceJSONImpl implements IStockServiceJSON {
 
	public List<StockJSON> getStocks() {
		
		List<StockJSON> stocks;
		
		try {
			stocks = new ObjectMapper()
					.readValue(this.getClass().getResourceAsStream("/stocks.json"), new TypeReference<List<StockJSON>>() {
					});
			
			return stocks;
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

}


